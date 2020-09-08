//Graham Zug (GVZ3)

#include <stdio.h>
#include <string.h>
#include <ctype.h>

void get_line(char* buffer, int size) {
	fgets(buffer, size, stdin);
	int len = strlen(buffer);
	// this is a little more robust than what we saw in class.
	if(len != 0 && buffer[len - 1] == '\n')
		buffer[len - 1] = '\0';
}

// returns 1 if the two strings are equal, and 0 otherwise.
int streq(const char* a, const char* b) {
	return strcmp(a, b) == 0;
}

// returns 1 if the two strings are equal ignoring case, and 0 otherwise.
// so "earth" and "Earth" and "EARTH" will all be equal.
int streq_nocase(const char* a, const char* b) {
	// hohoho aren't I clever
	for(; *a && *b; a++, b++) if(tolower(*a) != tolower(*b)) return 0;
	return *a == 0 && *b == 0;
}

float weight_on_planet(const char* planet_name, int user_weight){
	
	if(streq_nocase(planet_name, "mars")) {
	user_weight = user_weight*.38;
	return user_weight;
	}
	if(streq_nocase(planet_name, "venus")) {
        user_weight = user_weight*.91;
	return user_weight;
        }
	if(streq_nocase(planet_name, "mercury")) {
        user_weight = user_weight*.38;
	return user_weight;
        }
	if(streq_nocase(planet_name, "earth")) {
        user_weight = -3;	
	return user_weight;
        }
	if(streq_nocase(planet_name, "Jupiter")) {
        user_weight = user_weight*2.54;
	return user_weight;
        }
	if(streq_nocase(planet_name, "Saturn")) {
        user_weight = user_weight*1.08;
	return user_weight;
        }
	if(streq_nocase(planet_name, "Uranus")) {
        user_weight = user_weight*.91;
	return user_weight;
        }
	if(streq_nocase(planet_name, "Neptune")) {
        user_weight = user_weight*1.19;
	return user_weight;
        }
	if(streq_nocase(planet_name, "pluto")) {
        user_weight = -2;
	return user_weight;
        }
	if(streq_nocase(planet_name, "exit")) {
        user_weight = -4;
        return user_weight;
        }
	
user_weight = -1;
return user_weight;
}

int main() {
	

	
	printf("How much do you weigh? \n \n");
	char input[100];
	get_line(input, sizeof(input));
	
	int weight;
	sscanf(input, "%d", &weight); 
	
	while(1){
		printf("What planet would you like to visit (type 'exit' if you wish to exit) \n");
		
		char planet[100];
		get_line(planet, sizeof(planet));
		
		if(weight_on_planet(planet, weight) == -4){
                return 0;
                } else		
		if(weight_on_planet(planet, weight) == -3){
		printf("You weigh %d. None of it is brain, though. \n", weight);		
		} else
		if(weight_on_planet(planet, weight) == -2){
                printf("All_Around_Me_Are_Familiar_Faces.mp3 \n");
                } else
		if(weight_on_planet(planet, weight) == -1){
                printf("I hate to say it, but it looks like the planet you're searching for doesn't exist. (If an item doesn't appear in our reports, it doesn't exist.) \n");
                } else{
		printf("You'd weigh %f on ", weight_on_planet(planet, weight));
		printf(planet);
		printf("\n");
		}
	}
	return 0;
}
