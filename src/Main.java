public class Main {
    public static void main(String[] args) {
        final Node zero = new Node(0);
        final Node one = new Node(1);
        final Node two = new Node(2);
        final Node three = new Node(3);
        final Node four = new Node(4);

        zero.addEdge("a", one);
        zero.addEdge("a", three);
        one.addEdge("b", two);
        two.addEdge("b", one);
        two.addEdge("b", three);
        three.addEdge("a", one);
        three.addEdge("a", four);

        final Automaton automaton = new Automaton();
        automaton.addSymbol("a");
        automaton.addSymbol("b");

        automaton.addStart(zero);
        automaton.addFinal(four);

        System.out.println(automaton.invert());
    }
}