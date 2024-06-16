public class Main {
    public static boolean VERBOSE_MODE = false;

    public static void main(String[] args) {
        final Node one = new Node(1);
        final Node two = new Node(2);
        final Node three = new Node(3);
        final Node four = new Node(4);
        final Node five = new Node(5);
        final Node six = new Node(6);
        final Node seven = new Node(7);
        final Node eight = new Node(8);
        final Node nine = new Node(9);
        final Node ten = new Node(10);
        final Node eleven = new Node(11);

        one.addEdge("a", one);
        one.addEdge("b", two);
        two.addEdge("a", eleven);
        two.addEdge("b", three);
        three.addEdge("a", four);
        three.addEdge("b", five);
        four.addEdge("a", one);
        four.addEdge("b", six);
        five.addEdge("a", seven);
        five.addEdge("b", three);
        six.addEdge("a", four);
        six.addEdge("b", eight);
        seven.addEdge("a", seven);
        seven.addEdge("b", three);
        eight.addEdge("a", nine);
        eight.addEdge("b", eight);
        nine.addEdge("a", ten);
        nine.addEdge("b", six);
        ten.addEdge("a", ten);
        ten.addEdge("b", six);
        eleven.addEdge("a", eleven);
        eleven.addEdge("b", eleven);

        final Automaton automaton = new Automaton();
        automaton.addSymbol("a");
        automaton.addSymbol("b");

        automaton.addStart(one);
        automaton.addFinal(three);
        automaton.addFinal(four);
        automaton.addFinal(six);
        automaton.addFinal(eight);
        automaton.addFinal(nine);
        automaton.addFinal(ten);

        System.out.println(automaton.minimizeByBrzozowski(VERBOSE_MODE));
    }
}