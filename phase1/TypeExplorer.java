package phase1;

public class TypeExplorer {
    public static void main(String[] args) {
        // --- Primitive types ---
        byte myByte = 127; // 8-bit, range: -128 to 127
        short myShort = 32000; // 16-bit, range: -32,768 to 32,767
        int myInt = 2_000_000; // 32-bit (underscores for readability, like 2000000)
        long myLong = 9_000_000_000L; // 64-bit, note the 'L' suffix
        float myFloat = 3.14f; // 32-bit decimal, note the 'f' suffix
        double myDouble = 3.14159265; // 64-bit decimal (default for decimals)
        char myChar = 'A'; // 16-bit Unicode character (JS has no char type)
        boolean myBool = true; // true or false only (not truthy/falsy like JS!)
		
        // --- Print each value ---
        System.out.println("=== Primitive Types ===");
        System.out.println("byte:    " + myByte);
        System.out.println("short:   " + myShort);
        System.out.println("int:     " + myInt);
        System.out.println("long:    " + myLong);
        System.out.println("float:   " + myFloat);
        System.out.println("double:  " + myDouble);
        System.out.println("char:    " + myChar);
        System.out.println("boolean: " + myBool);

        // --- Wrapper classes & their limits ---
        // In JS, everything is Number. In Java, each type has its own range.
        System.out.println("\n=== Type Ranges ===");
        System.out.println("Byte range:    " + Byte.MIN_VALUE + " to " + Byte.MAX_VALUE);
        System.out.println("Short range:   " + Short.MIN_VALUE + " to " + Short.MAX_VALUE);
        System.out.println("Integer range: " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE);
        System.out.println("Long range:    " + Long.MIN_VALUE + " to " + Long.MAX_VALUE);
        System.out.println("Float range:   " + Float.MIN_VALUE + " to " + Float.MAX_VALUE);
        System.out.println("Double range:  " + Double.MIN_VALUE + " to " + Double.MAX_VALUE);

        // --- Autoboxing & Unboxing ---
        // Autoboxing: primitive → wrapper (automatic)
        Integer wrappedInt = myInt; // int → Integer (autoboxing)
        Double wrappedDouble = myDouble; // double → Double (autoboxing)

        // Unboxing: wrapper → primitive (automatic)
        int unwrappedInt = wrappedInt; // Integer → int (unboxing)
        double unwrappedDouble = wrappedDouble; // Double → double (unboxing)

        System.out.println("\n=== Autoboxing Demo ===");
        System.out.println("Autoboxed Integer: " + wrappedInt);
        System.out.println("Unboxed int: " + unwrappedInt);
        System.out.println("Are they equal? " + (myInt == unwrappedInt)); // true
        System.out.println("Are they identical? " + (myInt == wrappedInt));

        // ⚠️ GOTCHA: Comparing wrapper objects with == checks reference, not value!
        Integer a = 200;
        Integer b = 200;
        System.out.println("\n=== Wrapper Comparison Gotcha ===");
        System.out.println("a == b (reference):    " + (a == b)); // false! (for values > 127)
        System.out.println("a.equals(b) (value):   " + a.equals(b)); // true!
        // In JS, 200 === 200 is always true. In Java, use .equals() for wrapper
        // objects.
        int a_1 = 200;
        int b_1 = 200;
        System.out.println("a_1 == b_1 (primitive): " + (a_1 == b_1));

        // --- Type casting ---
        System.out.println("\n=== Type Casting ===");
        int bigNumber = 130;
        byte smallNumber = (byte) bigNumber; // Explicit cast: loses data! 130 overflows to -126
        System.out.println("int 130 cast to byte: " + smallNumber); // -126 (overflow!)

        double preciseNumber = 9.99;
        int truncated = (int) preciseNumber; // Truncates, does NOT round
        System.out.println("double 9.99 cast to int: " + truncated); // 9
    }
}


