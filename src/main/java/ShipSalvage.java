
import java.util.Scanner;

public class ShipSalvage {

    private static final char[][] exampleMap = {
            {'O', 'O', ' ', 'O', ' ', ' ', 'O', 'O', 'O', 'O'},
            {' ', ' ', ' ', 'O', ' ', ' ', ' ', ' ', ' ', ' '},
            {'O', ' ', ' ', 'O', ' ', 'O', 'O', 'O', ' ', 'O'},
            {'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O'},
            {'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {'O', ' ', ' ', ' ', ' ', 'O', ' ', ' ', ' ', 'O'},
            {' ', ' ', ' ', ' ', ' ', 'O', ' ', ' ', ' ', 'O'},
            {'O', 'O', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'O'},
            {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
            {' ', ' ', ' ', ' ', 'O', 'O', 'O', 'O', 'O', 'O'}
    };

    public static FieldState[][] getExample() { //type: FieldState + [][] see enum below, name: getExample
        FieldState[][] map = new FieldState[10][10]; // Create a new two-dimensional array of type FieldState with size 10x10

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                map[i][j] = FieldState.fromOutput(exampleMap[i][j]); // convert char element of exampleMap into a FieldState element
            }
        }

        return map; // Return the newly created map array
    }

    //Checks whether the map is valid
    public static void checkValidMap(FieldState[][] map) {
        if (map == null || map.length != 10) { //Checks map for 0
            throw new IllegalArgumentException("Invalid map: Cannot be null and must be 10x10");
        }

        //Checks every single entry in the map for null
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (map[i][j] == null) {
                    throw new IllegalArgumentException("Invalid map: Cannot be null and must be 10x10");
                }
            }

        }
    }

    public static boolean allSalvaged(FieldState[][] map) {
        for (FieldState[] fieldStatesZeile : map) {
            for (FieldState fieldStateSpalte : fieldStatesZeile) {
                if (fieldStateSpalte == FieldState.OCCUPIED_HIDDEN) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void printMap(FieldState[][] map, boolean showHidden) {
        checkValidMap(map);
        System.out.println("   ABCDEFGHIJ");
        System.out.println("  +----------+");
        for (int i = 0; i < 10; i++) {
            if (i==9) {
                System.out.print((i+1)+"|");
            } else {
                System.out.print((i+1)+" |");
            }
   for (int j = 0; j < 10; j++) {
                if (map[i][j] == FieldState.OCCUPIED_HIDDEN && !showHidden) { //showhidden is false,
                    System.out.print(FieldState.EMPTY.output); //The "output", i.e. ' ' of EMPTY, since there would actually be ship
                } else {
                    System.out.print(map[i][j].output); //the output that is actually there isshowhidden
                }
            }
            System.out.println("|");
        }
        System.out.println(" +----------+");
    }

    public static String probeField(FieldState[][] map, String field) {
        int z, s; //Row and column

        if (field.equals("bye"))
            System.exit(0);

        if (field == null || field.length() <2 || field.length() >3) {
            return "Cannot be empty";
        }
        if (field.isBlank()) {
            return "Must not be blank";
        }
        if (field.toLowerCase().charAt(0)<97 || field.toLowerCase().charAt(0) > 106) { //A-J
            return "Field does not exist"+field.toLowerCase().charAt(0);
        } else if (Character.getNumericValue(field.charAt(1))<1 ||Character.getNumericValue(field.charAt(1))>9) { //1-10
            return "Field does not exist" + Character.getNumericValue(field.charAt(1));
        }else if (field.length() == 3 && !(Character.getNumericValue(field.charAt(2))==0 && Character.getNumericValue(field.charAt(1))==1)){
            return "invalid number";

        }else {
            s = field.toLowerCase().charAt(0)-97; //split
            if(field.length() == 3 && (Character.getNumericValue(field.charAt(2))==0 && Character.getNumericValue(field.charAt(1))==1))
                z = 9;
            else
                z = Character.getNumericValue(field.charAt(1))-1;//line
            FieldState currentState = map[z][s];
            FieldState newState;

            switch (currentState) { // Update the field state based on the current state
                case EMPTY -> {
                    newState = FieldState.MISS;
                    map[z][s] = newState; // Update the field state in the map
                    return "Nothing found";
                }
                case OCCUPIED_HIDDEN -> {
                    newState = FieldState.OCCUPPIED_SALVAGED;
                    map[z][s] = newState; // Update the field state in the map
                    return "Wreck found!";
                }
                case OCCUPPIED_SALVAGED -> {
                    newState = FieldState.OCCUPPIED_SALVAGED;
                    map[z][s] = newState; // Update the field state in the map
                    return "Already examined!";
                }
                case MISS -> {
                    newState = FieldState.MISS;
                    map[z][s] = newState; // Update the field state in the map
                    return "Already examined!";
                }
                default -> throw new IllegalArgumentException("Invalid field state!");
            }

        }

    }


     public static void main(String[] args) {
         Scanner scanner = new Scanner(System.in);

         while (true) {
             System.out.println("new game? y/n/auto");
             if (!scanner.hasNext()) {
                 System.out.println("No valid input");
                 scanner.nextLine();
             } else{
                 String input = scanner.next(); //read input
                 switch (input) {
                     case "n":
                         break;

                     case "y":
                         FieldState[][] map = getExample();
                         // Game loop
                         printMap(map, false); //Print map
                         while (!allSalvaged(map)) {
                             System.out.println("Input:");
                             input = scanner.next();
                             String answer1 = probeField(map, input).toUpperCase(); //apply input in probefield
                             printMap(map, false); //Print map
                             System.out.println("- "+answer1+" -");
                         }
                         System.out.println("All ships recovered!");   // All ships salvaged
                         printMap(map, true); // print the map one last time
                         continue;

                     case "auto":
                         FieldState[][] map1 = getExample();
                         // Game loop
                         while (!allSalvaged(map1)) {
                             printMap(map1, false); //Print map
                             for (int i = 0; i <10;i++) {
                                 for (int j = 0; j <10;j++) {
                                     if (map1[i][j] == FieldState.OCCUPIED_HIDDEN){
                                         char c = (char) (j+97);
                                         input = String.valueOf(c) + (i+1);
                                         System.out.println("Input:");
                                         System.out.println("\""+input+"\"");
                                         String answer = probeField(map1, input).toUpperCase(); //apply intput to probefield
                                         printMap(map1, false); //Print map
                                         System.out.println("- "+answer+" -");
                                     }
                                 }
                             }
                         }
                         System.out.println("All ships recovered!");   // All ships salvaged
                         printMap(map1, true); // print the map one last time
                         continue;

                     default:
                         System.out.println("Invalid input");
                         continue;
                 }
                 break;
             }

         }

     }

}



