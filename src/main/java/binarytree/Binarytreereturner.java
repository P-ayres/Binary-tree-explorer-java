package binarytree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/*
 Levando em conta a especificação do desafio que entregou uma arvore n-ária, 
o presente código transforma a mesma em uma arvore binária, realiza o balanceamento 
e mostra em tela o número de nós nivel a nivel
execute utilizando mvn exec:java no terminal enquanto estiver na pasta do projeto para executar (arvorebinária)
*/

class GenericNode { 
    int value;
    List<GenericNode> children = new ArrayList<>();

    GenericNode(int value) {
        this.value = value;
    }
}

class BinaryNode { 
    int value;
    BinaryNode left, right;

    BinaryNode(int value) {
        this.value = value;
    }
}


public class Binarytreereturner {
    public static void main(String[] args) {
        GenericNode root = new GenericNode(1);
        GenericNode n10 = new GenericNode(10);
        GenericNode n20 = new GenericNode(20);
        GenericNode n50 = new GenericNode(50);
        GenericNode n25 = new GenericNode(25);
        GenericNode n35 = new GenericNode(35);

        root.children.addAll(Arrays.asList(n10, n20, n50));
        n20.children.addAll(Arrays.asList(n25, n35));

        // Serializar e desserializar JSON
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(root);

        GenericNode rootFromJson = gson.fromJson(json, GenericNode.class);
        BinaryNode binaryRoot = convertToBinary(rootFromJson);

        System.out.println("\nNíveis da Árvore Binária pré-balanceamento:");
        System.out.println(getLevelsofBinaryTree(binaryRoot));
        System.out.print("---------------------------------------------");

        BinaryNode balancedRoot = balanceBinaryTree(binaryRoot);

        System.out.println("\nNíveis da Árvore Binária Balanceada:");
        System.out.println(getLevelsofBinaryTree(balancedRoot));
    }

    // conversão da arvore n-ária para binária (esquerda para direita)
    static BinaryNode convertToBinary(GenericNode root) {
        if (root == null) return null;
        BinaryNode newRoot = new BinaryNode(root.value);

        if (root.children.size() > 0)
            newRoot.left = convertToBinary(root.children.get(0));

        BinaryNode current = newRoot.left;
        for (int i = 1; i < root.children.size(); i++) {
            current.right = convertToBinary(root.children.get(i));
            current = current.right;
        }

        return newRoot;
    }

    static List<List<Integer>> getLevelsofBinaryTree(BinaryNode root) {
        List<List<Integer>> levels = new ArrayList<>();
        if (root == null) return levels;

        Queue<BinaryNode> q = new LinkedList<>();
        q.add(root);

        while (!q.isEmpty()) {
            int size = q.size();
            List<Integer> current = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                BinaryNode node = q.poll();
                current.add(node.value);
                if (node.left != null) q.add(node.left);
                if (node.right != null) q.add(node.right);
            }
            levels.add(current);
        }
        return levels;
    }

    //realizar o balanceamento da arvore
    static BinaryNode balanceBinaryTree(BinaryNode root) {
        List<Integer> values = new ArrayList<>();
        binaryOrdenation(root, values);
        values.sort(Integer::compareTo);
        return buildBalancedBinaryTree(values, 0, values.size() - 1);
    }

    //retorna arvore ordenada
    static void binaryOrdenation(BinaryNode node, List<Integer> values) {
        if (node == null) return;
        binaryOrdenation(node.left, values);
        values.add(node.value);
        binaryOrdenation(node.right, values);
    }

    static BinaryNode buildBalancedBinaryTree(List<Integer> vals, int start, int end) {
        if (start > end) return null;
        int mid = (start + end) / 2;
        BinaryNode node = new BinaryNode(vals.get(mid));
        node.left = buildBalancedBinaryTree(vals, start, mid - 1);
        node.right = buildBalancedBinaryTree(vals, mid + 1, end);
        return node;
    }
}
