package practise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.regex.Pattern;

public class Main {
	public static void main(String[] args) {
     List<Integer> v= new ArrayList();
     v.add(23);
     v.add(33);
     v.add(43);
     v.add(22);
        int n=Collections.max(v);
        System.out.println(Collections.min(v));
        System.out.println(n);
        Collections.sort(v);
        System.out.println(v);
        Collections.reverse(v);
        System.out.println(v);
       System.out.println( Collections.frequency(v, 33));
       
       int[] arr= {23,53,64,54,28,99};
       List<Integer> ls=new ArrayList<Integer>();
       for(int num:arr) {
    	   System.out.print(num);
    	   ls.add(num);
       }
       List al=Arrays.asList(arr);
       
       
       ArrayList<Integer> al2= new ArrayList(Arrays.asList(11,22,33,44));
                ArrayList<Car> cr=new ArrayList<Car>();
                Car c1=new Car("punch", "black");
                cr.add(c1);
                System.out.println();
                Map<String,String>map= new HashMap<String,String>();
                map.put("name", "Sunil");
                map.put("id", "A1887");
                map.put("email", "sunil781@gmail.com");
                System.out.println(map.get("id"));
                
                
                // containsKey / containsValue
                System.out.println("Has id" + map.containsKey("id")); // true
                System.out.println("Has value 'Sunil'? " + map.containsValue("Sunil")); // true

                // keySet
                System.out.println("Keys: " + map.keySet());

                // values
                System.out.println("Values: " + map.values());

                // entrySet
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    System.out.println(entry.getKey() + " => " + entry.getValue());
                }

               
                // replace
                map.replace("name", "MS dhoni");

                // remove
                map.remove("email");

                // size and isEmpty
                System.out.println("Map size: " + map.size());
                System.out.println("Is map empty? " + map.isEmpty());

                // forEach
                map.forEach((key, value) -> System.out.println(key + ": " + value));

                // clear
                map.clear();
                System.out.println(" After clearing:" + map.isEmpty());
                
                
     
	}
}
