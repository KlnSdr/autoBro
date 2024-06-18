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

        var headline = document.createElement("h1");
        headline.setInnerText("autoBro");
        div.appendChild(headline);

        var lblNumStates = document.createElement("label");
        lblNumStates.setInnerText("Anzahl der Zustände:");
        div.appendChild(lblNumStates);

        HTMLInputElement txtNumStates = (HTMLInputElement) document.createElement("input");
        txtNumStates.setId("txtNumStates");
        txtNumStates.setType("number");
        txtNumStates.setValue("1");
        div.appendChild(txtNumStates);

        var newline = document.createElement("br");
        div.appendChild(newline);

        var lblNumSymbols = document.createElement("label");
        lblNumSymbols.setInnerText("Symbole (getrennt durch Komma):");
        div.appendChild(lblNumSymbols);

        HTMLInputElement txtSymbols = (HTMLInputElement) document.createElement("input");
        txtSymbols.setId("txtSymbols");
        txtSymbols.setValue("a,b");
        div.appendChild(txtSymbols);

        var newline2 = document.createElement("br");
        div.appendChild(newline2);

        var lblStartState = document.createElement("label");
        lblStartState.setInnerText("Startzustände (getrennt durch Komma):");
        div.appendChild(lblStartState);

        HTMLInputElement txtStartStates = (HTMLInputElement) document.createElement("input");
        txtStartStates.setId("txtStartStates");
        txtStartStates.setValue("1");
        div.appendChild(txtStartStates);

        var newline3 = document.createElement("br");
        div.appendChild(newline3);

        var lblFinalStates = document.createElement("label");
        lblFinalStates.setInnerText("Endzustände (getrennt durch Komma):");
        div.appendChild(lblFinalStates);

        HTMLInputElement txtFinalStates = (HTMLInputElement) document.createElement("input");
        txtFinalStates.setId("txtFinalStates");
        txtFinalStates.setValue("1");
        div.appendChild(txtFinalStates);

        var newline4 = document.createElement("br");
        div.appendChild(newline4);

        var divOutTable = document.createElement("div");
        HTMLTextAreaElement taOutput = (HTMLTextAreaElement) document.createElement("textarea");

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
