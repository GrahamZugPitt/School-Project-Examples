#include <linux/syscalls.h>
#include <linux/kernel.h>
#include <linux/uaccess.h>
#include <linux/spinlock.h>
#include <linux/stddef.h>
#include <linux/list.h>
#include <linux/slab.h>
#include <linux/cs1550.h>


/*Global variables:
 * sem_list is the linked list of all semaphores.
 *
 * global_semaphore_counter is a counter that begins at 1 and is used to set the ID of every semaphore created.
 * It is incremented everytime a new semaphore is created.
 *
 * global_rwlock is used for protecting the previously mentioned global variables, which can be read by many processes, but
 * only written to by one process at a time. 
 */
static LIST_HEAD(sem_list);
int global_semaphore_counter = 1;
static DEFINE_RWLOCK(global_rwlock);

/*Takes semaphore id number as an argument.
 *
 * Returns the semaphore associated with the id by traversing the global list.
 *
 * Returns NULL if it cannot find a semaphore with sem->sem_id = sem_id in the list.
 */
struct cs1550_sem* get_semaphore_from_list(int sem_id){
        struct cs1550_sem *sem = NULL;
        struct cs1550_sem *sem_desired = NULL;
	//CRITICAL SECTION. Reads the global semaphore list to try and find the correct semaphore.
	read_lock(&global_rwlock);
        	list_for_each_entry(sem, &sem_list, list){
                	if(sem->sem_id == sem_id){
                        	sem_desired = sem;
                	}
        	}
	read_unlock(&global_rwlock);
        return sem_desired;
}

/**
 * Creates a new semaphore. The long integer value is used to
 * initialize the semaphore's value.
 *
 * The initial `value` must be greater than or equal to zero.
 *
 * On success, returns the identifier of the created
 * semaphore, which can be used with up() and down().
 *
 * On failure, returns -EINVAL or -ENOMEM, depending on the
 * failure condition.
 */


SYSCALL_DEFINE1(cs1550_create, long, value)
{
	/*Gotta put the declarations up here cause this is C90 baybe*/
	struct cs1550_sem *sem = kmalloc(sizeof(struct cs1550_sem),0);

	/*Checking for errors*/
	if(sem == 0){
                return -ENOMEM;
        }

	if(value < 0){
		kfree(sem);
		return -EINVAL;
	}

	/*Setting values that are not in the critical section and initializing list heads*/
	spin_lock_init(&sem->lock);
        sem->value = value;

	INIT_LIST_HEAD(&sem->waiting_tasks); //Initializes this specific semaphore's list of waiting tasks
	INIT_LIST_HEAD(&sem->list); //Initializes the list_head 

	/*CRITICAL SECTION. Adding this semaphore to the global list and assigning it an ID.*/
	write_lock(&global_rwlock);
		sem->sem_id = global_semaphore_counter++;
		list_add_tail(&sem->list, &sem_list);
	write_unlock(&global_rwlock);


	return sem->sem_id;
}

/**
 * Performs the down() operation on an existing semaphore
 * using the semaphore identifier obtained from a previous call
 * to cs1550_create().
 *
 * This decrements the value of the semaphore, and *may cause* the
 * calling process to sleep (if the semaphore's value goes below 0)
 * until up() is called on the semaphore by another process.
 *
 * Returns 0 when successful, or -EINVAL or -ENOMEM if an error
 * occurred.
 */
SYSCALL_DEFINE1(cs1550_down, long, sem_id)
{
	/*Gets the requested semaphore, and creates a place for the task that represents this process to live.*/
	struct cs1550_task *task_node = kmalloc(sizeof(struct cs1550_task),0);
        struct cs1550_sem *sem = get_semaphore_from_list(sem_id);

	/*Error checking*/
	if(task_node == 0){
		return -ENOMEM;
	}
	if(sem == 0){
		kfree(task_node);
		return -EINVAL;
	}
	/*Initializes the task_node associated with this task*/
	task_node->task = current;
	INIT_LIST_HEAD(&task_node->list);

	/*CRITICAL SECTION. If the semaphore value is positive, decrements the semaphore and frees the task.*/
	if(sem->value > 0){
		spin_lock(&sem->lock);
                        sem->value--;
                spin_unlock(&sem->lock);
		kfree(task_node);
		return 0;
	}
	/*CRITICAL SECTION. If the semaphore value is non-positive, decrements the semaphore and adds the task to this semaphores queue.*/
	if(sem->value <= 0){
		spin_lock(&sem->lock);
			sem->value--;
			list_add_tail(&task_node->list, &sem->waiting_tasks);
		spin_unlock(&sem->lock);
		set_current_state(TASK_INTERRUPTIBLE);
		schedule();
	}
	return 0;
}

/**
 * Performs the up() operation on an existing semaphore
 * using the semaphore identifier obtained from a previous call
 * to cs1550_create().
 *
 * This increments the value of the semaphore, and *may cause* the
 * calling process to wake up a process waiting on the semaphore,
 * if such a process exists in the queue.
 *
 * Returns 0 when successful, or -EINVAL if the semaphore ID is
 * invalid.
 */
SYSCALL_DEFINE1(cs1550_up, long, sem_id)
{ 	/*Gets the semaphore this function is looking for and makes a placeholder for the task*/
	struct cs1550_sem *sem = get_semaphore_from_list(sem_id);
	struct cs1550_task *task = NULL;
	
	/*Error checking*/
	if(sem == 0){
		return -EINVAL;
	}
	/*CRITICAL SECTION. Increments the semaphore value. If a process is waiting on this semaphore, wakes up the process that queued first*/
	spin_lock(&sem->lock);
		sem->value++;
		if(!list_empty(&sem->waiting_tasks)){
			task = list_first_entry(&sem->waiting_tasks, struct cs1550_task, list);
			wake_up_process(task->task);
			list_del(&task->list);
			kfree(task);
		}
        spin_unlock(&sem->lock);
	return 0;
}

/**
 * Removes an already-created semaphore from the system-wide
 * semaphore list using the identifier obtained from a previous
 * call to cs1550_create().
 *
 * Returns 0 when successful or -EINVAL if the semaphore ID is
 * invalid or the semaphore's process queue is not empty.
 */
SYSCALL_DEFINE1(cs1550_close, long, sem_id)
{
	//Gets the desired semaphore. 
	struct cs1550_sem *sem = get_semaphore_from_list(sem_id);
	//Error checking.
	if(sem == 0){
		return -EINVAL;
	}
	//CRITICAL SECTION. Removes the semaphore from the global list. 
        write_lock(&global_rwlock);
                list_del(&sem->list);
        write_unlock(&global_rwlock);

	kfree(sem);
	return 0; 
}
