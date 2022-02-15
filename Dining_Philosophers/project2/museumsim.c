#include <pthread.h>
#include <stdlib.h>
#include <stdio.h>

#include "museumsim.h"

//
// In all of the definitions below, some code has been provided as an example
// to get you started, but you do not have to use it. You may change anything
// in this file except the function signatures.
//


struct shared_data {
	// Add any relevant synchronization constructs and shared state here.
	// For example:
		pthread_mutex_t ticket_mutex;
		pthread_mutex_t next_guide_mutex;
		pthread_mutex_t next_visitor_mutex;
		pthread_mutex_t visitors_in_museum_mutex;
		pthread_mutex_t visitors_waiting_mutex;
		pthread_mutex_t next_tour_mutex;

		pthread_cond_t visitor_can_enter_cond;
		pthread_cond_t guide_can_enter_cond;
		pthread_cond_t next_tour_cond;
		pthread_cond_t visitors_waiting_cond;
		pthread_cond_t guide_leaving_cond;

	    int tickets;
		int guides_in_museum;
		int visitors_in_museum;
		int visitors_waiting_outside;
		int guide_can_enter;
		int visitor_can_enter;
		int guide_done;
		int all_gone;
};

static struct shared_data shared;


/**
 * Set up the shared variables for your implementation.
 * 
 * `museum_init` will be called before any threads of the simulation
 * are spawned.
 */
void museum_init(int num_guides, int num_visitors)
{
	pthread_mutex_init(&shared.ticket_mutex, NULL);
	pthread_mutex_init(&shared.next_guide_mutex, NULL);
	pthread_mutex_init(&shared.next_visitor_mutex, NULL);
	pthread_mutex_init(&shared.visitors_in_museum_mutex, NULL);
	pthread_mutex_init(&shared.visitors_waiting_mutex, NULL);
	pthread_mutex_init(&shared.next_tour_mutex, NULL);


	pthread_cond_init(&shared.next_tour_cond, NULL);
	pthread_cond_init(&shared.visitor_can_enter_cond, NULL);
	pthread_cond_init(&shared.guide_can_enter_cond, NULL);
	pthread_cond_init(&shared.visitors_waiting_cond, NULL);
	pthread_cond_init(&shared.guide_leaving_cond, NULL);
	
	shared.tickets = MIN(VISITORS_PER_GUIDE * num_guides, num_visitors);
	shared.guides_in_museum = 0;
	shared.visitors_waiting_outside = 0;
	shared.visitors_in_museum = 0;
	shared.visitor_can_enter = -1;
	shared.guide_done = 0;
	shared.guide_can_enter = 1;
	shared.all_gone = 0;
}


/**
 * Tear down the shared variables for your implementation.
 * 
 * `museum_destroy` will be called after all threads of the simulation
 * are done executing.
 */
void museum_destroy()
{
	pthread_mutex_destroy(&shared.ticket_mutex);
	pthread_mutex_destroy(&shared.next_guide_mutex);
	pthread_mutex_destroy(&shared.next_visitor_mutex);
	pthread_mutex_destroy(&shared.visitors_in_museum_mutex);
	pthread_mutex_destroy(&shared.visitors_waiting_mutex);
	pthread_mutex_destroy(&shared.next_tour_mutex);

	pthread_cond_destroy(&shared.next_tour_cond);
	pthread_cond_destroy(&shared.visitor_can_enter_cond);
	pthread_cond_destroy(&shared.guide_can_enter_cond);
	pthread_cond_destroy(&shared.visitors_waiting_cond);
	pthread_cond_destroy(&shared.guide_leaving_cond);
}


/**
 * Implements the visitor arrival, touring, and leaving sequence.
 */
void visitor(int id)
{
	//Visitor arrives and attempts to get a ticket
	int got_ticket = 0;
	visitor_arrives(id);
	pthread_mutex_lock(&shared.ticket_mutex);
		pthread_mutex_lock(&shared.visitors_waiting_mutex);
			if(shared.tickets > 0){
				shared.tickets = shared.tickets - 1; 
				shared.visitors_waiting_outside++; //tickets + waiting will always reflect the true sum of these two values because the action of decrementing and incrementing is atomic
				got_ticket = 1;
				pthread_cond_signal(&shared.visitors_waiting_cond);
			}
		pthread_mutex_unlock(&shared.visitors_waiting_mutex);
	pthread_mutex_unlock(&shared.ticket_mutex);
	//If the visitor cannot get their ticket, they leave :(
	if(got_ticket == 0){
		visitor_leaves(id);
		return;
	}
	//Visitor goes into a state in which they are waiting outside
	pthread_mutex_lock(&shared.next_visitor_mutex);
		while(shared.visitor_can_enter < 1){
			pthread_cond_wait(&shared.visitor_can_enter_cond, &shared.next_visitor_mutex);
		}
		//Waiting ends and the visitor enters
		shared.visitor_can_enter--;
		if(shared.visitor_can_enter == 0 && shared.guide_done == 1){
			shared.visitor_can_enter = -1;
			shared.guide_done = 0;
		}
	pthread_mutex_unlock(&shared.next_visitor_mutex);
	//visitor tours and signals future actors
	visitor_tours(id); 
	pthread_cond_signal(&shared.next_tour_cond);
	pthread_cond_signal(&shared.visitor_can_enter_cond);
	//Visitor leaves
	pthread_mutex_lock(&shared.visitors_in_museum_mutex);
		shared.visitors_in_museum--;
		pthread_cond_signal(&shared.guide_leaving_cond);
		visitor_leaves(id);
	pthread_mutex_unlock(&shared.visitors_in_museum_mutex);
}

/**
 * Implements the guide arrival, entering, admitting, and leaving sequence.
 */
void guide(int id)
{
	int tour_count = 0;
	//Arrival. 
	guide_arrives(id);
	//Guides check if they are allowed inside.
	pthread_mutex_lock(&shared.next_guide_mutex);
		while(shared.guide_can_enter == 0){
			pthread_cond_wait(&shared.guide_can_enter_cond, &shared.next_guide_mutex);
		}
		guide_enters(id);
		shared.guides_in_museum++;
		if(shared.guides_in_museum == GUIDES_ALLOWED_INSIDE){
			shared.guide_can_enter = 0;
		}
	pthread_mutex_unlock(&shared.next_guide_mutex);

	//The first guide that gets past this block will let either 10 visitors or the rest of the remaining visitors in
	pthread_mutex_lock(&shared.next_tour_mutex);
		while(shared.visitor_can_enter > -1){ 
			pthread_cond_wait(&shared.next_tour_cond, &shared.next_tour_mutex);
		}
	shared.visitor_can_enter = 0;
	pthread_mutex_unlock(&shared.next_tour_mutex);
	//Admission loop
	while(tour_count < VISITORS_PER_GUIDE){	
		if(shared.all_gone == 1){
			shared.visitor_can_enter = -1;
			break;
		}
		//Checks to see if everyone has been let in - if yes, remaining people tour and future guides simply enter and leave
		pthread_mutex_lock(&shared.ticket_mutex);
			pthread_mutex_lock(&shared.visitors_waiting_mutex);
				if(shared.visitors_waiting_outside + shared.tickets == 0){
					pthread_mutex_lock(&shared.next_tour_mutex);
						shared.guide_done = 1;
						shared.all_gone = 1;
					pthread_mutex_unlock(&shared.next_tour_mutex);
					pthread_mutex_unlock(&shared.visitors_waiting_mutex);
					pthread_mutex_unlock(&shared.ticket_mutex);
					break;
				}
			pthread_mutex_unlock(&shared.visitors_waiting_mutex);
		pthread_mutex_unlock(&shared.ticket_mutex);
		//Guide waits here for visitors to arrive
		pthread_mutex_lock(&shared.visitors_waiting_mutex);
			while(shared.visitors_waiting_outside == 0){
				pthread_cond_wait(&shared.visitors_waiting_cond, &shared.visitors_waiting_mutex);
			}
		pthread_mutex_unlock(&shared.visitors_waiting_mutex);
		//Visitors are admitted in this block, taking them out of the waiting queue and into the museum
		pthread_mutex_lock(&shared.next_visitor_mutex);	
		pthread_mutex_lock(&shared.visitors_in_museum_mutex);
				guide_admits(id);
					pthread_mutex_lock(&shared.visitors_waiting_mutex);
					shared.visitors_in_museum++;
					shared.visitors_waiting_outside--;
					pthread_mutex_unlock(&shared.visitors_waiting_mutex);
				pthread_mutex_unlock(&shared.visitors_in_museum_mutex);	
				tour_count++;
				shared.visitor_can_enter++;
		pthread_mutex_unlock(&shared.next_visitor_mutex);
			
		pthread_cond_broadcast(&shared.visitor_can_enter_cond);
	}
	//This signals the next guide to begin taking guests in
	pthread_mutex_lock(&shared.next_visitor_mutex);
		shared.guide_done = 1;
	pthread_mutex_unlock(&shared.next_visitor_mutex);
	
	//This checks if everyone has left, if yes, the guide can leave
	pthread_mutex_lock(&shared.visitors_in_museum_mutex); 
			while(shared.visitors_in_museum > 0){
				pthread_cond_wait(&shared.guide_leaving_cond, &shared.visitors_in_museum_mutex);
			}
	pthread_mutex_unlock(&shared.visitors_in_museum_mutex);
	pthread_cond_signal(&shared.guide_leaving_cond);
	pthread_cond_signal(&shared.next_tour_cond);
	//This ensures that both guides leave together
	pthread_mutex_lock(&shared.next_guide_mutex);
		shared.guides_in_museum--;
		if(shared.guides_in_museum == 0){
			shared.guide_can_enter = 1;
			pthread_cond_signal(&shared.guide_can_enter_cond);
		}
		guide_leaves(id);
	pthread_mutex_unlock(&shared.next_guide_mutex);
}
