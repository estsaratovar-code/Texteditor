import java.util.*;
import java.io.*;
import com.murcia.utils.*;
class Main {
    public static void main(String[] args) throws Exception{
        File file=new File("randomtext.txt");
        Scanner sc=new Scanner(file);
        ListaEnlazada<StringBuilder> l=new ColaEnlazada<StringBuilder>();
        while(sc.hasNextLine()) {
            String line=sc.nextLine();
            String[] words=line.split("\\s");
            for(String i:words) 
                l.add(new StringBuilder(i));
            l.add(new StringBuilder("\n"));
        }
        Editor edit=new Editor(l);
        sc=new Scanner(System.in);
       while(true)
{
    System.out.println("S - Save and Exit");
    System.out.println("F - Move Forward");
    System.out.println("B - Move Backward");
    System.out.println("b - Move to Beginning of Line");
    System.out.println("e - Move to End of Line");
    System.out.println("i - Insert");
    System.out.println("d - Delete");

    edit.print();
    edit.printCursor();

    String input=sc.nextLine();

    if(input.equals("S")) {
        edit.save();   
        break;
    }
    else if(input.equals("F")) edit.forward();
    else if(input.equals("B")) edit.backward();
    else if(input.equals("b")) edit.beginning();
    else if(input.equals("e")) edit.ending();
    else if(input.equals("i")) edit.insert(sc.nextLine());
    else if(input.equals("d")) edit.delete();

    System.out.print("\033[H\033[2J");  
    System.out.flush();
}
        edit.writer.close();
    }
}
class Editor {
    ListaEnlazada<StringBuilder> l;
    int node,position;
    FileWriter writer;
    public Editor(ColaEnlazada<StringBuilder> l) {
        this.l=l;
        node=position=0;
    }
  public void forward(){
    if(l.isEmpty()) return;

    StringBuilder s = l.get(node);

    if(position >= s.length()) {
        node = (node + 1) % l.size();
        position = 0;
    } else {
        position++;
    }

    if(l.get(node).toString().equals("\n")) {
        node = (node + 1) % l.size();
        position = 0;
    }
}
  public void backward(){
    if(l.isEmpty()) return;

    if(position == 0) {
        node = (node - 1 + l.size()) % l.size();
        position = l.get(node).length();
    } else {
        position--;
    }

    if(l.get(node).toString().equals("\n")) {
        node = (node - 1 + l.size()) % l.size();
        position = l.get(node).length();
    }
}
    public void insert(String input) {
        StringBuilder s=l.get(node);
        if(s.length()==0) s.append(input);
        else s.insert(position,input);
        position+=input.length();
    }
    public void delete() {
    if(l.isEmpty()) return;

    StringBuilder s = l.get(node);

    if(position == 0 && node > 0){
        StringBuilder prev = l.get(node-1);
        position = prev.length();
        prev.append(s);
        l.remove(node);
        node--;
    }
    else if(position > 0){
        s.delete(position-1, position);
        position--;
    }
}
    public void save() throws Exception {
    FileWriter writer = new FileWriter("randomtext.txt");
    for(StringBuilder i : l) {
        writer.write(i.toString());
        if(!i.toString().equals("\n")) {
            writer.write(" ");
        }
    }
    writer.close();
}
    public void beginning() {
       while(node>0 && !l.get(node).toString().equals("\n")) node--;
       if(node!=0) node=node+1;
       position=0;
    }
    public void ending() {
    while(node < l.size() && 
          !l.get(node).toString().equals("\n")) {
        node++;
    }

    if(node > 0) node--;
    position = l.get(node).length();
}
   public void print() throws Exception{
       writer= new FileWriter("randomtext.txt");
    for(int i=0,i<1.size();i++) {
       writer.write(l.get(i).toString());
        System.out.print (i);
        if(!i.toString().equals("\n")) {
            writer.write(" ");
            System.out.print(" ");
        }
    }
}
    public void printCursor() {
        System.out.print("Cursor At : ");
        StringBuilder s=l.get(node);
        for(int i=0; i<s.length(); i++) {
            if(i==position) System.out.print("|");
            System.out.print(s.charAt(i));
        }
        if((position==0 && s.length()==0) || (position==s.length())) System.out.print("|");
        System.out.println();
    }
}
