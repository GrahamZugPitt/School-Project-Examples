//Graham Zug (GVZ3)


#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>

typedef struct Node{
	int value;
	struct Node* next;
}Node;

Node* create_node(int value){
	Node* node = malloc(sizeof(int*)*2);
	node->value = value;
	node->next = NULL;
	return node;
}

void list_print(Node* head){
	while(head != NULL){
		printf("%i", head->value);
		if(head->next != NULL){
			printf("->");
		}
		head = head->next;
	}
	printf("\n");
	printf("\n");

}

Node* list_append(Node* head, int value){
	while(head->next != NULL){
		head = head->next;
		}
	head->next = create_node(value);
	return head->next;
}

Node* list_prepend(Node*head, int value){
	Node* newNode = create_node(value);
	newNode->next = head;
	return newNode; 	
}

void list_free(Node* head){
	while(head != NULL){
	printf("test");
	Node* theNodeAfterTheHead = head->next;
	free(head);
	head = theNodeAfterTheHead;
	}
}

Node* list_remove(Node* head, int value){
	if(head->next == NULL){
		if(head->value == value){
			return NULL;
		}else{
			return head;
		}
		
	}	
	
	if(head->value == value){
                Node* theNodeAfterTheHead = head->next;
                free(head);
                return theNodeAfterTheHead;

        }
	
		Node* trueHead = create_node(0);
                trueHead->value = head->value;
                trueHead->next = head->next;


	
	while(head->next != NULL){

		if(head->next->value == value){
			Node* theNodeAfterTheNodeAfterTheHead = head->next->next;
			free(head->next);
			head->next = theNodeAfterTheNodeAfterTheHead;
			return trueHead;
		}
		head = head->next;
	}
	if(head->value == value){
                free(head);
                return trueHead;
	}
	return trueHead;
}


int main(){
// The comments at the ends of the lines show what list_print should output.
	Node* head = create_node(1);
	list_print(head);                  // 1
	Node* end = list_append(head, 2);
	list_print(head);                  // 1 -> 2
	end->next = create_node(3);
	list_print(head);                  // 1 -> 2 -> 3
	head = list_prepend(head, 0);
	list_print(head);                  // 0 -> 1 -> 2 -> 3
	list_append(head, 4);
	list_print(head);                  // 0 -> 1 -> 2 -> 3 -> 4
	list_append(head, 5);
	list_print(head);                  // 0 -> 1 -> 2 -> 3 -> 4 -> 5

	head = list_remove(head, 5);
	list_print(head);                  // 0 -> 1 -> 2 -> 3 -> 4
	head = list_remove(head, 3);
	list_print(head);                  // 0 -> 1 -> 2 -> 4
	head = list_remove(head, 0);
	list_print(head);                  // 1 -> 2 -> 4

	//list_free(head);

	return 0;
}
