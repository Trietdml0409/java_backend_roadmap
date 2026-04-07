package phase1;

public class StringManipulation {
    public static void main(String[] args) {
        String fullName = "John Michael Doe ";

        // --- Initials ---
        String initials = getInitials(fullName);
        System.out.println("Initials: " + initials); // J.M.D.

        // --- Reversed ---
        String reversed = reverseString(fullName);
        System.out.println("Reversed: " + reversed); // eoD leahciM nhoJ

        // --- Word count ---
        int wordCount = countWords(fullName);
        System.out.println("Word count: " + wordCount); // 3
    }

    /**
     * Extract initials from a full name.
     * "John Michael Doe" → "J.M.D."
     *
     * JS equivalent:
     * fullName.split(' ').map(w => w[0]).join('.') + '.'
     */
    static String getInitials(String name) {
        // split() works just like JS, but uses regex by default
        String[] words = name.split(" ");

        // StringBuilder is like an array you join later — much faster than string += in
        // a loop
        // In JS, you might use array.push() then .join(). Same idea.
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            // charAt(0) is like word[0] in JS
            sb.append(word.charAt(0));
            sb.append(".");
        }

        return sb.toString();
    }

    /**
     * Reverse a string.
     * "John Michael Doe" → "eoD leahciM nhoJ"
     *
     * JS equivalent:
     * fullName.split('').reverse().join('')
     */
    static String reverseString(String str) {
        // Java has a built-in way — StringBuilder has a reverse() method!
        return new StringBuilder(str).reverse().toString();

        // Manual approach (for learning):
        // char[] chars = str.toCharArray();
        // int left = 0, right = chars.length - 1;
        // while (left < right) {
        // char temp = chars[left];
        // chars[left] = chars[right];
        // chars[right] = temp;
        // left++;
        // right--;
        // }
        // return new String(chars);
    }

    /**
     * Count words in a string.
     * "John Michael Doe" → 3
     *
     * JS equivalent:
     * fullName.trim().split(/\s+/).length
     */
    static int countWords(String str) {
        // trim() removes leading/trailing spaces (same as JS)
        // split("\\s+") splits on one or more whitespace characters (regex)
        String trimmed = str.trim();

        // Handle empty string edge case
        if (trimmed.isEmpty()) {
            return 0;
        }

        return trimmed.split("\\s+").length;
        
    }
}