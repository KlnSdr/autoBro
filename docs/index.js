class Automaton {
  starts = [];
  finals = [];
  symbols = [];

  addStart(node) {
    this.starts.push(node);
  }

  addSymbol(symbol) {
    this.symbols.push(symbol);
  }

  addFinal(node) {
    this.finals.push(node);
  }

  getSymbols() {
    return this.symbols;
  }

  getStarts() {
    return this.starts;
  }

  getFinals() {
    return this.finals;
  }

  getNodes() {
    const visited = [];
    const knownNodes = [];

    this.starts.forEach((node) => this.traverse(node, visited, knownNodes));
    return knownNodes;
  }

  traverse(currentNode, visited, knownNodes) {
    if (visited.includes(currentNode.getName())) {
      return;
    }

    visited.push(currentNode.getName());
    knownNodes.push(currentNode);

    Object.keys(currentNode.getAllEdges()).forEach((key) => {
      currentNode.getEdges(key).forEach((node) => {
        this.traverse(node, visited, knownNodes);
      });
    });
  }

  toString() {
    let output = "NFA {\n";

    output += 'alphabet = mkSet "';
    output += this.getSymbols().join("");
    output += '",\n';

    output +=
      "states = mkSet [" +
      this.getNodes()
        .map((n) => n.getName())
        .join(",") +
      "],\n";

    output +=
      "starts = mkSet [" +
      this.getStarts()
        .map((n) => n.getName())
        .join(",") +
      "],\n";

    output +=
      "finals = mkSet [" +
      this.getFinals()
        .map((n) => n.getName())
        .join(",") +
      "],\n";

    output += "trans = collect [\n";

    const allTrans = [];
    this.getNodes().forEach((n) => {
      n.getDelta().forEach((trans) => allTrans.push(trans));
    });

    output += allTrans.join("\n,");

    output += "]\n";
    output += "}";

    return output;
  }

  toDFA() {
    const dfa = new Automaton();
    return dfa;
  }

  invert() {
    const inverted = new Automaton();
    return inverted;
  }

  minimize() {
    return this.toDFA().invert().toDFA().invert().toDFA();
  }
}

class Node {
  name = 0;
  edges = {};

  constructor(name) {
    this.name = name;
  }

  getName() {
    return this.name;
  }

  addEdge(symbol, node) {
    if (!Object.keys(this.edges).includes(symbol)) {
      this.edges[symbol] = [];
    }
    this.edges[symbol].push(node);
  }

  getAllEdges() {
    return this.edges;
  }

  getEdges(symbol) {
    if (!Object.keys(this.edges).includes(symbol)) {
      return [];
    }
    return this.edges[symbol];
  }

  getDelta() {
    const delta = [];
    Object.keys(this.edges).forEach((key) => {
      this.edges[key].forEach((node) => {
        delta.push(`(${this.getName()}, '${key}', ${node.getName()})`);
      });
    });

    return delta;
  }
}

function start() {
  const one = new Node(1);
  const two = new Node(2);
  const three = new Node(3);
  const four = new Node(4);
  const five = new Node(5);
  const six = new Node(6);
  const seven = new Node(7);
  const eight = new Node(8);
  const nine = new Node(9);
  const ten = new Node(10);
  const eleven = new Node(11);

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

  const automaton = new Automaton();
  automaton.addSymbol("a");
  automaton.addSymbol("b");

  automaton.addStart(one);
  automaton.addFinal(three);
  automaton.addFinal(four);
  automaton.addFinal(six);
  automaton.addFinal(eight);
  automaton.addFinal(nine);
  automaton.addFinal(ten);

  automaton.toDFA();
}

start();
