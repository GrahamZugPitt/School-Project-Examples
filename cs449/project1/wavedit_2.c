//Graham Zug (GVZ3)
#include <stdio.h>
#include <string.h>
#include <ctype.h>
#include <stdlib.h>
#include <stdint.h>
#include <stddef.h>

typedef struct WavyBoi{

	char riff_id[4];
	unsigned int file_size; 
	char wave_id[4];
	char fmt_id[4];
	unsigned int fmt_size;
	uint16_t data_format;
	uint16_t number_of_channels;
	unsigned int samples_per_second;
	unsigned int bytes_per_second;
	uint16_t block_alignment;
	uint16_t bits_per_sample;
	char data_id[4];
	unsigned int data_size;
	

} WavyBoi;

int streq(const char* a, const char* b) {
	return strcmp(a, b) == 0;
}

int isTheFileAWAV(WavyBoi* psound){
	for(int i = 0; i < 4; i++){
	char RIFF[4] = "RIFF";
	if(psound->riff_id[i] != RIFF[i]){
		printf("Wrong file format! \n");
		exit(0);
	}
}

for(int i = 0; i < 4; i++){
	char WAVE[4] = "WAVE";
	if(psound->wave_id[i] != WAVE[i]){
		printf("Wrong file format! \n");
		exit(0);
	}
}

for(int i = 0; i < 4; i++){
	char fmt[4] = "fmt ";
	if(psound->fmt_id[i] != fmt[i]){
		printf("Wrong file format! \n");
		exit(0);
	}
}

for(int i = 0; i < 4; i++){
	char data[4] = "data";
	if(psound->data_id[i] != data[i]){
		printf("Wrong file format! \n");
		exit(0);
	}
}

if(psound->fmt_size != 16){
		printf("Wrong file format! \n");
		exit(0);
	}
if(psound->data_format != 1){
		printf("Wrong file format! \n");
		exit(0);
	}
if(psound->number_of_channels != 1 && psound->number_of_channels != 2){
		printf("Wrong file format! \n");
		exit(0);
	}
if(psound->samples_per_second < 0 || psound->samples_per_second > 192000){
		printf("Wrong file format! \n");
		exit(0);
	}
if(psound->bits_per_sample != 8 && psound->bits_per_sample != 16){
		printf("Wrong file format! \n");
		exit(0);
	}
if(psound->bytes_per_second != psound->samples_per_second * (psound->bits_per_sample)/8 * psound->number_of_channels){
		printf("Wrong file format! \n");
		exit(0);
	}
if(psound->block_alignment != (psound->bits_per_sample)/8 * psound->number_of_channels){
		printf("Wrong file format! \n");
		exit(0);
	}
printf("This is a %i-bit %iHz ", psound->bits_per_sample, psound->samples_per_second );

if(psound->number_of_channels == 2){
	printf("stereo \n");
}

if(psound->number_of_channels == 1){
	printf("mono \n");
}

printf("It is %i samples (%0.3f seconds) long.", (psound->data_size/psound->block_alignment), (float)(psound->data_size/psound->block_alignment)/(psound->samples_per_second));




return 1;
}

int doesTheFileExist(char fileName[]){

	FILE* soundFile = fopen(fileName, "rb+");
        if (soundFile == NULL){
                printf("Cannot locate file! \n");
                exit(0);
        }
	fclose(soundFile);
	return 1;
}

void readMe(){
	printf("\n wavedit: \n");
	printf("Description: Read information, change the speed, or reverse WAV files. \n \n");
	printf("wavedit 				displays this help page. \n");
	printf("wavedit FILENAME 			will give information about the file in question \n");
	printf("wavedit FILENAME -rate [SPEED]		will change the speed of the sound of the file. Please enter an integer between 0-192000. \n");
	printf("wavedit FILENAME -reverse		will change the file to that it plays backwards. \n");
}


int main(int argc, char** argv){
	
	if(argc == 0){
	readMe(); //describes functionality of this utility
	}

	int len = 0;
	char fileName[100];
	if(argc > 1){
		len = strlen(argv[1]);
		for(int i = 0; i < len; i++, argv[1]++){ //Gets file name
		fileName[i] = *argv[1];
			}
		}
	WavyBoi sound;
	if(doesTheFileExist(fileName) && argc == 2){
		
		FILE* soundFile = fopen(fileName, "rb+");
		fread(&sound, 4, 11, soundFile);
		WavyBoi* pointer = &sound;
		isTheFileAWAV(pointer);
	}
	

	
	

	return 0;	
}
