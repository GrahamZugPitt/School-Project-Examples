/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/


public class MyLZW {
    private static final int R = 256;        // number of input chars
    private static int L = 512;       // number of codewords = 2^W
    private static int LMax = 131072;
    private static int W = 9;         // codeword width

    public static void compress(String [] args) { 
	if(args[1].charAt(0) != 'n' && args[1].charAt(0) != 'm' && args[1].charAt(0) != 'r'){
		throw new IllegalArgumentException("Illegal command line argument");
	}
        String input = BinaryStdIn.readString();
        BinaryStdOut.write(args[1].charAt(0));
        TST<Integer> st = new TST<Integer>();
        for (int i = 0; i < R; i++)
            st.put("" + (char) i, i);
        int code = R+1;  // R is codeword for EOF
        float uncompressedDataProcessed = 0;
        float compressedDataGenerated = 0; 
        boolean isFull = false;
        float compressionRatioAtIsFull = 0;
        float theCompressionRatio = 0;

        while (input.length() > 0) {
            
            String s = st.longestPrefixOf(input);  // Find max prefix match s.
            uncompressedDataProcessed = uncompressedDataProcessed + (s.length() * 8);
            BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
            compressedDataGenerated = compressedDataGenerated + W;
	    theCompressionRatio = uncompressedDataProcessed/compressedDataGenerated;
            int t = s.length();
            if (t < input.length() && code < L)    // Add s to symbol table.
                st.put(input.substring(0, t + 1), code++);
            input = input.substring(t);            // Scan past s in input.
            if(args[1].equals("m") && isFull && compressionRatioAtIsFull/theCompressionRatio > 1.1){ 
                isFull = false;
                st = new TST<Integer>();
                for (int i = 0; i < R; i++)
                    st.put("" + (char) i, i); //resets the tree
                    code = R+1;  // R is codeword for EOF
                    W = 9;
                    L = 512;
            }
            if(code == L && W <= 16){
                if(W!=16){
                L = L * 2;
                W = W + 1; //Checks to see if the new word is the word at the edge. If yes, double L and now all code words are W + 1 long
                } else if (args[1].equals("r")){
	
                    st = new TST<Integer>();
                    for (int i = 0; i < R; i++)
                        st.put("" + (char) i, i); //resets the tree
                        code = R+1;  // R is codeword for EOF
                        W = 9;
                        L = 512;
                } else if (args[1].equals("m") && !isFull){
                    
                    isFull = true;
                    compressionRatioAtIsFull = theCompressionRatio;
                }
            }
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 


    public static void expand() {
        String[] st = new String[LMax];
        int i; // next available codeword value
        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
	char typeOfCompression = BinaryStdIn.readChar();
        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];
        float uncompressedDataGenerated = 0;
        float compressedDataProcessed = W; 
        boolean isFull = false;
        float compressionRatioAtIsFull = 0;
        float theCompressionRatio = 0;

        while (true) {
            
            BinaryStdOut.write(val);
            uncompressedDataGenerated = uncompressedDataGenerated + val.length()*8;
	    theCompressionRatio = uncompressedDataGenerated/compressedDataProcessed;
	    
            if(typeOfCompression == 'm' && isFull && compressionRatioAtIsFull/theCompressionRatio > 1.1){
		isFull = false;
                for (i = 0; i < R; i++)
                    st[i] = "" + (char) i;
                st[i++] = "";   
                W = 9;
                L = 512;
		codeword = BinaryStdIn.readInt(W);
        	if (codeword == R) return;          
                val = st[codeword];
		BinaryStdOut.write(val);
		uncompressedDataGenerated = uncompressedDataGenerated + val.length()*8;
		compressedDataProcessed = compressedDataProcessed + W;
            }
		if(i == L - 1 && W <= 16){
                    if(W != 16){
                        L = L * 2;                  //Checks to see if the previously recorded word was the word at the edge. If yes, double L and now all code words are W + 1 long
                        W = W + 1;
                    } else if (typeOfCompression == 'r'){
                        st = new String[LMax];
                        for (i = 0; i < R; i++)
                            st[i] = "" + (char) i;
                        st[i++] = "";   
                        W = 9;
                        L = 512;
			codeword = BinaryStdIn.readInt(W);
        		if (codeword == R) return;          
                        val = st[codeword];
			BinaryStdOut.write(val);
                    } else if (typeOfCompression == 'm' && !isFull){
                    isFull = true;
                    compressionRatioAtIsFull = theCompressionRatio;
		    
                }
                    
            }
            codeword = BinaryStdIn.readInt(W);
            compressedDataProcessed = compressedDataProcessed + W;
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
		
            if (i < L) st[i++] = val + s.charAt(0); 
            val = s;
        }
        BinaryStdOut.close();
    }



    public static void main(String[] args) {
        if      (args[0].equals("-")) compress(args);
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }

}
