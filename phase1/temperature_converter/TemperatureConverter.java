package phase1.temperature_converter;
import java.util.Scanner;



public class TemperatureConverter {

    public static void Converter(String type, String degree){
        float result;
        
        if(type.equals("F")){
            System.out.println("Converting to Celcius");

            try {
                float value = Float.parseFloat(degree);
            } catch (NumberFormatException e) {
                System.out.printf("Degree must be a valid number\n");
                return;
            }

            result = (Float.parseFloat(degree)-32) * 5/9;
            System.out.printf("It is %.2f C \n",result);
                
        }

        else if(type.equals("C")){
            System.out.println("Converting to Farenheit");
            try {
                float value = Float.parseFloat(degree);
            } catch (NumberFormatException e) {
                System.out.printf("Degree must be a valid number\n");
                return;
            }

            result = (Float.parseFloat(degree) * 9/5) + 32;
            System.out.printf("It is %.2f C\n",result);

        }else{
            System.out.print("Temperature type must be F or C");
        }

    }

    public static void main(String[] args){
        //The user_input
        String user_input_degree;
        String user_input_type;
        Scanner input = new Scanner(System.in);

        boolean running = true;
        while(running){
            
            System.out.print("Type temperature degree, Q to quit: ");
            user_input_degree = input.nextLine().toUpperCase();

            if(user_input_degree.equals("Q")){
                running = false;
                break;
            }


            System.out.print("Type the type of the input degree, F for Farenheit and C for celcius: ");
            user_input_type = input.nextLine().toUpperCase();

            TemperatureConverter.Converter(user_input_type, user_input_degree);


            
        }
        System.out.print("Program end");
    }
    
}