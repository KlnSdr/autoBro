public class Main {
    public static void main(String[] args) {
        final Node one = new Node(1);
        final Node two = new Node(2);
        final Node three = new Node(3);
        final Node four = new Node(4);
        final Node five = new Node(5);
        final Node six = new Node(6);

        one.addEdge("b", one);
        one.addEdge("a", three);
        two.addEdge("b", one);
        two.addEdge("b", four);
        three.addEdge("a", two);
        three.addEdge("a", four);
        three.addEdge("b", six);
        four.addEdge("b", five);
        five.addEdge("b", three);
        five.addEdge("b", six);
        five.addEdge("a", one);
        six.addEdge("a", two);

        final Automaton automaton = new Automaton();
        automaton.addSymbol("a");
        automaton.addSymbol("b");

        automaton.addStart(one);
        automaton.addFinal(one);

        System.out.println(automaton.minimizeByBrzozowski());
    }
}