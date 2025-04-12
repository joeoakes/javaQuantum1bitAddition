/*
q0 (a) and q1 (b) are initialized to 1 using X gates.
Two CNOT gates compute the XOR (sum = a ⊕ b) into q2.
A Toffoli gate computes the AND (carry = a AND b) into q3.
 */
import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

public class QuantumAdder {

    public static void main(String[] args) {
        // Qubits: a (0), b (1), sum (2), carry (3)
        // Four Qubits
        Program p = new Program(4);

        // Step 1: Set Qubits a = 1, b = 1
        Step init = new Step();
        init.addGates(new X(0), new X(1));
        p.addStep(init);

        // Step 2: sum = a XOR b (part 1)
        // CNOT = Controlled NOT, Control qubit: The qubit that “decides” whether to activate the gate.
        // Target qubit: The qubit that gets flipped (NOT operation) if the control qubit is 1
        Step xor1 = new Step();
        xor1.addGate(new Cnot(0, 2)); // a ⊕ sum
        p.addStep(xor1);

        // Step 3: sum = a XOR b (part 2)
        Step xor2 = new Step();
        xor2.addGate(new Cnot(1, 2)); // b ⊕ sum
        p.addStep(xor2);

        // Step 4: carry = a AND b (Toffoli)
        // Toffoli Gate also known as the CCNOT gate (Controlled-Controlled-NOT)
        // The Toffoli gate flips (applies a NOT/X gate to) the target qubit only if both control qubits are 1.
        Step carry = new Step();
        carry.addGate(new Toffoli(0, 1, 3));
        p.addStep(carry);

        // Execute the program
        SimpleQuantumExecutionEnvironment sqee = new SimpleQuantumExecutionEnvironment();
        Result result = sqee.runProgram(p);

        Qubit[] qubits = result.getQubits();
        System.out.println("Results:");
        System.out.println("a = " + qubits[0].measure());
        System.out.println("b = " + qubits[1].measure());
        System.out.println("sum = " + qubits[2].measure());
        System.out.println("carry = " + qubits[3].measure());
    }
}
