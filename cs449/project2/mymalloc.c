



#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

#include "mymalloc.h"

// USE THIS GODDAMN MACRO OKAY
#define PTR_ADD_BYTES(ptr, byte_offs) ((void*)(((char*)(ptr)) + (byte_offs)))

// Don't change or remove these constants.
#define MINIMUM_ALLOCATION  16
#define SIZE_MULTIPLE       8


unsigned int round_up_size(unsigned int data_size) {
	if(data_size == 0)
		return 0;
	else if(data_size < MINIMUM_ALLOCATION)
		return MINIMUM_ALLOCATION;
	else
		return (data_size + (SIZE_MULTIPLE - 1)) & ~(SIZE_MULTIPLE - 1);
}




typedef struct BlockHead{ //Something something Charlie Brown
	unsigned int size;
	struct BlockHead* next;
	struct BlockHead* prev;
	int isFree;
}BlockHead; //The struct for heads of Blocks.

//*Notices your global variable* OwO what's this

BlockHead* theHead = NULL;
BlockHead* theTail = NULL;

BlockHead newBlockHead(unsigned int size){
        BlockHead newBlock = {size, NULL, NULL, 0};
        return newBlock;
}

void addOnToTheHeap(void* p, BlockHead blockHead){
	BlockHead* blockHeadPointer = p;
	*blockHeadPointer = blockHead;
}

BlockHead* makeHeapPointer(void* p){
	BlockHead* blockHeadPointer = p;
	return blockHeadPointer;
}

BlockHead* makeHeapPointerBH(BlockHead* p){
        BlockHead* blockHeadPointer = p;
        return blockHeadPointer;
}

void* addOnAnEmptyHeap(BlockHead* theBlockHead, unsigned int size){

	void* p = sbrk(sizeof(BlockHead) + size); //points to the previous break location
        BlockHead* heapPointerInitial = makeHeapPointer(p); //Uses a void* to make a BlockHead*


      	addOnToTheHeap(p, *theBlockHead); //Puts initial addition on the heap
        theHead = heapPointerInitial; //Moves the head
        theTail = heapPointerInitial; //Moves the tail
        p = PTR_ADD_BYTES(heapPointerInitial, sizeof(*theBlockHead)); //Puts the pointer where he need to go


        return p; //A mundane return statement. Nothing else is going on here. This comment was a waste of both your time and mine.

}


BlockHead* findNextEmptySpace(BlockHead* heapPointer, unsigned int size){
	BlockHead* tempHeapPointer = heapPointer;
	while(tempHeapPointer->next != NULL){
		if(tempHeapPointer->isFree == 1 && tempHeapPointer->size > size){
			return tempHeapPointer;
		}
		tempHeapPointer = tempHeapPointer->next;
	}
	return NULL;
}

void* addOnTheEnd(unsigned int size){
	void* theEnd = sbrk(sizeof(BlockHead) + size); //points to the previous break location
	BlockHead theBlockHead = newBlockHead(size);

	addOnToTheHeap(theEnd, theBlockHead); 
	BlockHead* p = makeHeapPointer(theEnd); //Some pointer casting
	theTail->next = p;
	p->prev = theTail; //linking the list
	theTail = theTail->next;
	theEnd = PTR_ADD_BYTES(theEnd, sizeof(BlockHead));
	return theEnd;
	
}

void* my_malloc(unsigned int size) {
	if(size == 0)
		return NULL;
	
	size = round_up_size(size);

	// ------- Don't remove anything above this line. -------
		
	BlockHead theBlockHead = newBlockHead(size); //makes a new BlockHead for this memory
	BlockHead* pointsToNewBlockHead = &theBlockHead; //Ow oof owie very pointy
	BlockHead* heapPointer = makeHeapPointerBH(theHead); 
	void* p;	
	
	
	if(heapPointer == NULL || (heapPointer->isFree == 1 && theHead == theTail)){
		p = addOnAnEmptyHeap(pointsToNewBlockHead, size);
	} else{
		heapPointer = findNextEmptySpace(heapPointer, size);
		if(heapPointer != NULL){
			heapPointer->next = heapPointer + heapPointer->size + sizeof(BlockHead); //THIS IS WHERE YOU LEFT OFF DON'T FORGET THIS IS WHERE YOU LEFT OFF YOU STILL NEED TO EDIT THIS PART OF THE CODE AAAAAAAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
			p = heapPointer;
			p = PTR_ADD_BYTES(p, sizeof(theBlockHead)); //Puts the pointer where he need to go
			} else {
			 p = addOnTheEnd(size);
			return p;		
		}

	}

	return p; //A mundane return statement. Nothing else is going on here. This comment was a waste of both your time and mine.
}

void contractTheBreak(int size){
	if (theTail->prev != NULL){
                 theTail = theTail->prev;
       	} else {
                 theTail = NULL;
        }
	sbrk(-size - sizeof(BlockHead));
	if(theTail == NULL){
                 theHead = NULL;
        }

}

void my_free(void* ptr) {
	if(ptr == NULL)
		return;

	ptr = PTR_ADD_BYTES(ptr, (-sizeof(BlockHead))); //Places pointer position properly 
	BlockHead* p = ptr; //Castes cre... I'm done with alteration this changes a void pointer into a Blockhead pointer
	p->isFree = 1; //Frees the pointer
	if(theTail == p){
		contractTheBreak(p->size); //This handles contracting the break and also does some pointer arithmetic that only needs to be done in the tail case
	} 
	
}
