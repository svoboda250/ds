/* CIS 27: Lab 1
 * Union-Find Maze Implementation:
 * @author Jenny Hamer
 * 
 * Maze is represented by a 2D rectangular grid consisting of a 2D array of in-
 * dividual linked-lists that are randomly unioned together to generate the maze
 * 
 * there is to be one and only one path between any two given points  
 * --> results in one solution through the maze (random generation)
 * rows --> number of cells horizontally; columns --> number of cells vertically
 */

package lab1;

import java.util.*;

public class Maze {

    private final int rowCount; // N number of rows
    private final int columnCount; // M number of columns
    public int uniqueCells; // no. of individual cells; done when decremented->1
    public int[][] id; // stores the id of each cell: [0][0] --> 1, [0][1] --> 2
    public boolean[][] topWall; // [][] -> index of cell; true if wall exists
    public boolean[][] leftWall;
    public boolean[][] enteredCell; // true if cell has already been accessed
    public int entrance; // randomly selected entrance & exit
    public int exit;

    // constructor --> initialize an N by M grid of cells 
    public Maze(int N, int M) {
        columnCount = N; // horizontal aspect
        rowCount = M;    // vertical aspect
        uniqueCells = N * M;
        int count = 0;
        id = new int[columnCount][rowCount];

        // initialize the contents of the N by M array to values: 0 - (N*M - 1)
        while (count < uniqueCells) {
            for (int j = 0; j < rowCount; j++) {
                for (int i = 0; i < columnCount; i++) {
                    id[i][j] = count;
                    count++;
                }
            }
        }

        // establish the walls between the cells --> all initially exist
        topWall = new boolean[columnCount][rowCount];
        leftWall = new boolean[columnCount][rowCount];
        enteredCell = new boolean[columnCount][rowCount];
        for (int i = 0; i < columnCount; i++) {
            for (int j = 0; j < rowCount; j++) { 
                topWall[i][j] = true;
                leftWall[i][j] = true;
                enteredCell[i][j] = false;
            }
        }
        // entrance & exit random creation --> orientation designed for top & 
        // bottom entrance/exits (topWall of top row, leftWall of last row)
        Random r2 = new Random();
        int rand1 = r2.nextInt(columnCount);
        entrance = id[rand1][0];
        int rand2 = r2.nextInt(columnCount);
        exit = id[rand2][rowCount-1];
        System.out.println("The maze begins in cell " + entrance + " and finishes in cell " + exit);

        constructMaze();
    }

    public int find(int i, int j) {
        return id[i][j];
    }

    public boolean connectedCells(int i, int j, int u, int v) {
        return find(i, j) == find(u, v);
    }

    public void union(int i, int j, int u, int v) {
        int ijID = find(i, j);
        int uvID = find(u, v);
        // if the components are already in the same set, do nothing...
        if (ijID == uvID) {
            return;
        } // otherwise, rename (i,j)'s component to (u,v)'s name
        for (int x = 0; x < columnCount; x++) {
            for (int y = 0; y < rowCount; y++) {
                if (id[x][y] == ijID) {
                    id[x][y] = uvID;
                    enteredCell[x][y] = true;
                }
            }
        }
        uniqueCells--;
//        System.out.println("union of " + ijID + " & " + uvID); // testing
//        System.out.println(uniqueCells);                       // testing
    }

    public void constructMaze(int i, int j) {
        // if either N or M value is less than one, maze is invalid
        // or if N & M = 1 --> a 1 x 1 or single-cell maze, no walls to break
        enteredCell[i][j] = true;
        
        if ((columnCount < 1) || (rowCount < 1) || (columnCount == 1)
                && (rowCount == 1)) {
            System.out.println("This maze is either a single cell, or invalid "
                    + "dimensions were entered.");
            return;
        }
        Random r = new Random();
        // if current cell has un-unioned neighboring cells, randomly select 1
        // of them to union, knock down mutual wall (top or left), then move to
        // that cell
        while (uniqueCells > 1) {
            // cases for the perimeter cells which have two 
            int twoRand = r.nextInt(2);
            if (i == columnCount - 1 && j > 0) { // if cell on far right column
                if (!enteredCell[i-1][j]) { 	// check left
                    leftWall[i][j] = false;
                    System.out.println(find(i, j) + "'s left wall was removed");
                    union(i, j, i - 1, j);
                    constructMaze(i - 1, j);
                    break;
                } else if (!enteredCell[i][j-1]) { // check up
                    topWall[i][j] = false;
                    System.out.println(find(i, j) + "'s top wall was removed");
                    union(i, j, i, j - 1);
                    constructMaze(i, j - 1);
                    break;
                } else {
                    if (twoRand == 0) {
                        constructMaze(i - 1, j);
                    } else {
                        constructMaze(i, j - 1);
                    }
                }
            } else if (j == 0 && i > 0) {		// if cell on top row
                if (!enteredCell[i][j + 1]) {	// check down
                    topWall[i][j+1] = false;
                    System.out.println(find(i, j+1) + "'s top wall was removed");
                    union(i, j, i, j + 1);
                    constructMaze(i, j + 1);
                    break;
                } else if (!enteredCell[i - 1][j]) { // check left
                    leftWall[i][j] = false;
                    System.out.println(find(i, j) + "'s left wall was removed");
                    union(i, j, i - 1, j);
                    constructMaze(i - 1, j);
                    break;
                } else {
                    if (twoRand == 0) {
                        constructMaze(i, j + 1);
                    } else {
                        constructMaze(i - 1, j);
                    }
                }
            } else if (i == 0 && j < rowCount - 1) { // if cell on far left
                if (!enteredCell[i+1][j]) { 		// check right
                    leftWall[i + 1][j] = false;
                    System.out.println(find(i+1, j) + "'s left wall was removed");
                    union(i, j, i + 1, j);
                    constructMaze(i + 1, j);
                    break;
                } else if (!enteredCell[i][j + 1] && j != rowCount-1) { // check down
                    topWall[i][j + 1] = false;
                    System.out.println(find(i, j+1) + "'s top wall was removed");
                    union(i, j, i, j + 1);
                    constructMaze(i, j + 1);
                    break;
                } else {
                    if (twoRand == 0) {
                        constructMaze(i + 1, j);
                    } else {
                        constructMaze(i, j + 1);
                    }
                }
            } else if (j == rowCount - 1 && i < columnCount - 1) { // if bottom
                if (!enteredCell[i][j - 1]) { // check up
                    topWall[i][j] = false;
                    System.out.println(find(i, j) + "'s top wall was removed");
                    union(i, j, i, j - 1);
                    constructMaze(i, j - 1);
                    break;
                } else if (!enteredCell[i + 1][j]) { // check right
                    leftWall[i + 1][j] = false;
                    System.out.println(find(i+1, j) + "'s left wall was removed");
                    union(i, j, i + 1, j);
                    constructMaze(i + 1, j);
                    break;
                } else {
                    if (twoRand == 0) {
                        constructMaze(i, j - 1);
                    } else {
                        constructMaze(i + 1, j);
                    }
                }
            } else {
                int randomCell = r.nextInt(4); // generates nums 0-3 
                // unions & moves to cell above current
                if (randomCell == 0 && !enteredCell[i][j - 1]) { // check up
                    topWall[i][j] = false;
                    System.out.println(find(i, j) + "'s top wall was removed");
                    union(i, j, i, j - 1);
                    constructMaze(i, j - 1);
                    break;
                } // unions & moves to cell to the left of current 
                else if (randomCell == 1 && !enteredCell[i-1][j]) { // check l
                    leftWall[i][j] = false;
                    System.out.println(find(i, j) + "'s left wall was removed");
                    union(i, j, i - 1, j);
                    constructMaze(i - 1, j);
                    break;
                } // unions & moves to cell below current
                else if (randomCell == 2 && !enteredCell[i][j + 1]) {
                    topWall[i][j+1] = false;
                    System.out.println(find(i, j+1) + "'s top wall was removed");
                    union(i, j, i, j + 1);
                    constructMaze(i, j + 1);
                    break;
                } // unions & moves to cell right of current
                else if (randomCell == 3 && !enteredCell[i + 1][j]) {
                    leftWall[i + 1][j] = false;
                    System.out.println(find(i+1, j) + "'s left wall was removed");
                    union(i, j, i + 1, j);
                    constructMaze(i + 1, j);
                    break;
                }
                if (enteredCell[i][j - 1]
                        && enteredCell[i - 1][j]
                        && enteredCell[i][j + 1]
                        && enteredCell[i + 1][j]) {
                    if (randomCell == 0) {
                        constructMaze(i + 1, j);
                    } else if (randomCell == 1) {
                        constructMaze(i - 1, j);
                    } else if (randomCell == 2) {
                        constructMaze(i, j + 1);
                    } else {
                        constructMaze(i, j - 1);
                    }
                }
            }
        }
    }

    private void constructMaze() {
        // randomly generates starting cell coordinates & begins recursive 
        // (de-)construction process by then calling the constructMaze method
        // with inputs randomStartI, randomStartJ
        Random r = new Random();
        // generate random numbers between 0 and dimension (non-inclusive)
        int randomStartI = r.nextInt(columnCount); // row/i coordinate
        int randomStartJ = r.nextInt(rowCount); // column/j coordinate
        constructMaze(randomStartI, randomStartJ);
    }

    public static void main(String args[]) {
        Maze myMaze = new Maze(10, 10);
    }
}

/* Sample output:
run:
The maze begins in cell 0 and finishes in cell 94
95's top wall was removed
96's left wall was removed
96's top wall was removed
86's top wall was removed
76's left wall was removed
75's left wall was removed
74's top wall was removed
64's left wall was removed
63's left wall was removed
62's top wall was removed
52's left wall was removed
51's left wall was removed
60's top wall was removed
61's left wall was removed
71's top wall was removed
72's left wall was removed
73's left wall was removed
83's top wall was removed
84's left wall was removed
94's top wall was removed
87's left wall was removed
88's left wall was removed
89's left wall was removed
89's top wall was removed
79's left wall was removed
78's top wall was removed
68's top wall was removed
59's left wall was removed
59's top wall was removed
49's left wall was removed
48's left wall was removed
47's left wall was removed
46's top wall was removed
36's top wall was removed
26's left wall was removed
25's left wall was removed
24's top wall was removed
15's left wall was removed
16's left wall was removed
17's left wall was removed
27's top wall was removed
37's top wall was removed
38's left wall was removed
38's top wall was removed
28's top wall was removed
18's top wall was removed
8's left wall was removed
7's left wall was removed
6's left wall was removed
5's left wall was removed
4's left wall was removed
13's top wall was removed
13's left wall was removed
12's top wall was removed
2's left wall was removed
11's top wall was removed
11's left wall was removed
20's top wall was removed
21's left wall was removed
22's left wall was removed
32's top wall was removed
33's left wall was removed
34's left wall was removed
44's top wall was removed
45's left wall was removed
55's top wall was removed
55's left wall was removed
54's left wall was removed
53's top wall was removed
43's left wall was removed
42's left wall was removed
41's top wall was removed
31's left wall was removed
40's top wall was removed
70's top wall was removed
80's top wall was removed
81's left wall was removed
91's top wall was removed
92's left wall was removed
92's top wall was removed
90's top wall was removed
93's left wall was removed
93's top wall was removed
66's left wall was removed
67's left wall was removed
77's top wall was removed
97's top wall was removed
98's left wall was removed
99's left wall was removed
99's top wall was removed
69's top wall was removed
39's top wall was removed
29's top wall was removed
19's top wall was removed
57's top wall was removed
57's left wall was removed
56's left wall was removed
35's top wall was removed
23's left wall was removed
BUILD SUCCESSFUL (total time: 1 second)
*/