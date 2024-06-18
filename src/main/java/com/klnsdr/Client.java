package com.klnsdr;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLTextAreaElement;
import org.teavm.jso.dom.xml.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Client {
    public static void main(String[] args) {
        var document = HTMLDocument.current();
        var div = document.createElement("div");

        var tableStyle = document.createElement("table"); // peak web design

        var headline = document.createElement("h1");
        headline.setInnerText("Automaten minimieren");
        div.appendChild(headline);

        var lblNumStates = document.createElement("label");
        lblNumStates.setInnerText("Anzahl der Zustände:");

        HTMLInputElement txtNumStates = (HTMLInputElement) document.createElement("input");
        txtNumStates.setId("txtNumStates");
        txtNumStates.setType("number");
        txtNumStates.setValue("2");

        tableStyle.appendChild(styleTableRow(lblNumStates, txtNumStates));

        var lblNumSymbols = document.createElement("label");
        lblNumSymbols.setInnerText("Symbole (getrennt durch Komma):");

        HTMLInputElement txtSymbols = (HTMLInputElement) document.createElement("input");
        txtSymbols.setId("txtSymbols");
        txtSymbols.setValue("a,b");

        tableStyle.appendChild(styleTableRow(lblNumSymbols, txtSymbols));

        var lblStartState = document.createElement("label");
        lblStartState.setInnerText("Startzustände (getrennt durch Komma):");

        HTMLInputElement txtStartStates = (HTMLInputElement) document.createElement("input");
        txtStartStates.setId("txtStartStates");
        txtStartStates.setValue("0");

        tableStyle.appendChild(styleTableRow(lblStartState, txtStartStates));

        var lblFinalStates = document.createElement("label");
        lblFinalStates.setInnerText("Endzustände (getrennt durch Komma):");

        HTMLInputElement txtFinalStates = (HTMLInputElement) document.createElement("input");
        txtFinalStates.setId("txtFinalStates");
        txtFinalStates.setValue("1");

        tableStyle.appendChild(styleTableRow(lblFinalStates, txtFinalStates));

        div.appendChild(tableStyle);

        var divOutTable = document.createElement("div");
        HTMLTextAreaElement taOutput = (HTMLTextAreaElement) document.createElement("textarea");
        taOutput.setCols(90);
        taOutput.setRows(20);

        var btnGenerate = document.createElement("button");
        btnGenerate.setInnerText("übernehmen");
        btnGenerate.addEventListener("click", e -> {
            var numStates = Integer.parseInt(txtNumStates.getValue());
            var symbols = txtSymbols.getValue().split(",");
            var startStates = txtStartStates.getValue().split(",");
            var finalStates = txtFinalStates.getValue().split(",");

            divOutTable.clear();
            final HashMap<Integer, HashMap<String, ArrayList<String>>> transitions = buildTable(symbols, numStates, divOutTable);

            var bttnMinimize = document.createElement("button");
            bttnMinimize.setInnerText("Minimieren");
            bttnMinimize.addEventListener("click", e2 -> {
                taOutput.setValue("");
                var automaton = buildAutomaton(startStates, finalStates, symbols, numStates, transitions);
                taOutput.setValue(automaton.minimize().toString());
            });
            divOutTable.appendChild(bttnMinimize);
        });
        div.appendChild(btnGenerate);
        div.appendChild(divOutTable);
        div.appendChild(taOutput);

        document.getBody().appendChild(div);
    }

    private static Node styleTableRow(Node lbl, Node element) {
        var tr = HTMLDocument.current().createElement("tr");
        var td1 = HTMLDocument.current().createElement("td");
        td1.appendChild(lbl);
        tr.appendChild(td1);

        var td2 = HTMLDocument.current().createElement("td");
        td2.appendChild(element);
        tr.appendChild(td2);

        return tr;
    }

    private static Automaton buildAutomaton(String[] starts, String[]finals, String[] symbols, int numStates, HashMap<Integer, HashMap<String, ArrayList<String>>> transitions) {
        final Automaton automaton = new Automaton();
        final ArrayList<com.klnsdr.Node> nodes = new ArrayList<>();

        for (int i = 0; i < numStates; i++) {
            nodes.add(new com.klnsdr.Node(i));
        }

        for (String start : starts) {
            automaton.addStart(nodes.get(Integer.parseInt(start)));
        }
        for (String finalState : finals) {
            automaton.addFinal(nodes.get(Integer.parseInt(finalState)));
        }
        for (String symbol : symbols) {
            automaton.addSymbol(symbol);
        }


        for (var entry : transitions.entrySet()) {
            var state = entry.getKey();
            var stateTransitions = entry.getValue();

            var node = nodes.get(Integer.parseInt(String.valueOf(state)));
            for (var symbol : symbols) {
                var targetStates = stateTransitions.get(symbol);
                if (targetStates == null) {
                    continue;
                }
                for (var targetState : targetStates) {
                    node.addEdge(symbol, nodes.get(Integer.parseInt(targetState)));
                }
            }
        }

        return automaton;
    }

    private static HashMap<Integer, HashMap<String, ArrayList<String>>> buildTable(String[] symbols, int numStates, Node parent) {
        var document = HTMLDocument.current();
        var table = document.createElement("table");
        var thead = document.createElement("thead");
        var tbody = document.createElement("tbody");

        var tr = document.createElement("tr");
        var th = document.createElement("th");
        th.setInnerText("Zustand");
        tr.appendChild(th);

        for (var symbol : symbols) {
            th = document.createElement("th");
            th.setInnerText(symbol);
            tr.appendChild(th);
        }

        thead.appendChild(tr);
        table.appendChild(thead);

        final HashMap<Integer, HashMap<String, ArrayList<String>>> transitions = new HashMap<>();

        for (int i = 0; i < numStates; i++) {
            tr = document.createElement("tr");
            var td = document.createElement("td");
            td.setInnerText(String.valueOf(i));
            tr.appendChild(td);

            for (var symbol : symbols) {
                td = document.createElement("td");

                HTMLInputElement input = (HTMLInputElement) document.createElement("input");
                int finalI = i;
                input.addEventListener("input", e -> {
                    var value = input.getValue();
                    if (value.isEmpty()) {
                        return;
                    }

                    var targetStates = value.split(",");
                    var targets = new ArrayList<>(Arrays.asList(targetStates));

                    var stateTransitions = transitions.computeIfAbsent(finalI, k -> new HashMap<>());

                    stateTransitions.put(symbol, targets);
                });
                td.appendChild(input);

                tr.appendChild(td);
            }

            tbody.appendChild(tr);
        }

        table.appendChild(tbody);
        parent.appendChild(table);

        return transitions;
    }
}
