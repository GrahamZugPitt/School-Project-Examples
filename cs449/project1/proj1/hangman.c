//Graham Zug(GVZ3)

#include <stdio.h> 
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <time.h>

int streq_nocase(const char* a, const char* b, int length, int length2) {
	if(length != length2){
	return 0;
	}
	for(int i = 0; i < length; i++, a++, b++) if(tolower(*a) != tolower(*b)) return 0;
	return 1;
}

void get_line(char* input, int size, FILE* f) {
	fgets(input, size, f);
	int len = strlen(input);

	// this line of code *might* have a bug in it.
	// think about what value of "len" would cause a problem.
	input[len - 1] = '\0';
}



int main(int argc, char** argv){

	char theWord[20];
        int len = 0;
	
	if(argc > 1){
		len = strlen(argv[1]);
		for(int i = 0; i < len; i++, argv[1]++){
			theWord[i] = *argv[1];
		}
		
	}else{
	
	srand((unsigned int)time(NULL));
	FILE* words = fopen("dictionary.txt","r");
	if(words == NULL){
		printf("That file doesn't exist!\n");
		return 1;
	}
		

	int i = 0;
	while(!feof(words)){
	int dicLength;
	int chooseAWord;
	char line[20];
	fgets(line, sizeof(line), words);

	if(atoi(line) != 0){
	dicLength = atoi(line);
	chooseAWord = rand() % (1913 - 1) + 1;
	}
	if(i == chooseAWord){
	strcpy(theWord, line);
	len = strlen(theWord) - 2; // gets the length of the word
	break;
	}
	i++;
	}
	fclose(words);
	}
	printf("Welcome to Hangman! Your word has %i letters \n", len);
	
	int guessedLetters[20] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};	
	int strikes = 0;
	while(strikes < 5){
	int solutionChecker = 1;	

	for(int i = 0; i < len; i++){
		if (guessedLetters[i] == 0){
                        solutionChecker = 0;
                }
	}
	if(solutionChecker == 1){
		printf("You got it! The word was ");
                        printf(theWord);
                        return 0;

	}
	for(int i = 0; i < len; i++){
		if (guessedLetters[i] == 0){
			printf("_ ");
		}
		if (guessedLetters[i] == 1){
			printf("%c ", theWord[i]);
		}
	}	
	printf("Guess a letter or type the whole word: ");
	int guessLen = 0;
	char guess[20];
	get_line(guess, sizeof(guess), stdin);
	guessLen = strlen(guess);
	if(guessLen == 1){
		int correct = 0;
		for(int i = 0; i < len; i++){
			if (theWord[i] == *guess){
				correct = 1;
				guessedLetters[i] = 1;
			}
		}
		if(correct == 0){
			strikes++;
                        printf("Strike %i!", strikes);

		}
	}else{
		if(streq_nocase(guess, theWord, len, guessLen)){
			printf("You got it! The word was ");
			printf(theWord);
			return 0;
		}else{
			strikes++;
			printf("Strike %i!", strikes);
		}
	}
	printf("\n");
	
	}	
	printf("Sorry, you lose! The word was ");
	printf(theWord);
	printf("\n");
}

