/**
 * 
 * Assignment: Lab 4 Faces
 *
 */

package faces;

public class Faces {

    public static void main(String[] args) {
        // DRAWS 4 FACES BY USING METHODS
        drawFace1();
        System.out.println("\n"); // ADD WHITE SPACE BETWEEN FACES TO MAKE IT EASIER TO READ

        drawFace2();
        System.out.println("\n");

        drawFace3();
        System.out.println("\n");

        drawFace4();
    }

    // CREATES PROFILES OF DIFFERENT FACIAL FEATURES
    public static void drawFace1() {
        drawHair1();
        drawEyes2();
        drawMouth3();
    }

    public static void drawFace2() {
        drawHair4();
        drawEyes5();
        drawMouth6();
    }

    public static void drawFace3() {
        drawHair7();
        drawEyes8();
        drawMouth9();
    }

    public static void drawFace4() {
        drawHair0();
        drawEyes1();
        drawMouth2();
    }

    // HAIR TYPES
    public static void drawHair1() {
        System.out.println("  ######  ");
        System.out.println(" ######## ");
        System.out.println("/#__  __#\\");
    }

    public static void drawHair2() {
        System.out.println("  @@@@@@  ");
        System.out.println(" @@@@@@@@ ");
        System.out.println("@@__  __@@");
    }

    // EYE TYPES
    public static void drawEyes1() {
        System.out.println("| []  [] |");
        System.out.println("\\   /\\   /");
    }

    public static void drawEyes2() {
        System.out.println("{ <>  <> }");
        System.out.println("\\   ##   /");
    }

    // MOUTH TYPES
    public static void drawMouth1() {
        System.out.println(" \\  ##  / ");
        System.out.println("  \\ == /  ");
        System.out.println("   \\__/   ");
    }

    public static void drawMouth2() {
        System.out.println(" \\      / ");
        System.out.println("  \\ ~~ /  ");
        System.out.println("   \\__/   ");
    }
    
// END OF PUBLIC CLASS
}
