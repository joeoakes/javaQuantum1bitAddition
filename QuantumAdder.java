import org.redfx.strange.*;
import org.redfx.strange.gate.*;
import org.redfx.strange.local.SimpleQuantumExecutionEnvironment;

import java.util.Arrays;

public class QuantumAdder {

    public static void main(String[] args) {
        // Qubits: a (0), b (1), sum (2), carry (3)
        Program p = new Program(4);

        // Set a = 1, b = 1
        Step init = new Step();
        init.addGate(new X(0)); // a
        init.addGate(new X(1)); // b
        p.addStep(init);

        // Step for sum = a XOR b (use CNOT)
        Step xor = new Step();
        xor.addGate(new Cnot(0, 2)); // a -> sum
        xor.addGate(new Cnot(1, 2)); // b -> sum
        p.addStep(xor);

        // Step for carry = a AND b (Toffoli)
        Step and = new Step();
        and.addGate(new Toffoli(0, 1, 3)); // a,b -> carry
        p.addStep(and);

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
