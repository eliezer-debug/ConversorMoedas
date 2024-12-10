package br.com.alura;

import java.util.Scanner;

import com.google.gson.Gson;

import java.net.URI;
import java.net.http.*;

public class Conversor {

    private String chaveAPI;

    public String origem;
    
    public String destino;

    public String getChaveAPI() {
        return this.chaveAPI;
    }


    public void setChaveAPI(String chaveAPI) {
        this.chaveAPI = chaveAPI;
    }


    public static void menuMoeda() throws InterruptedException
    {
        System.out.println("Digite a moeda de que deseja converter: ");
        Thread.sleep(300);
        System.out.println("1. Real BRL (R$)");
        System.out.println("2. Dolár USD ($)");
        System.out.println("3. EURO EUR (€)");
        System.out.println("4. Libras GBP  (£)");
        System.out.println("5. Yenes JPY (¥)");
        System.out.println("6. Sair");
        Thread.sleep(100);
        System.out.print("Escolha uma opção: ");
    }


    public void app()
    {
        System.out.println("=====================================");
        System.out.println("     CONVERSOR DE MOEDAS - 2024");
        System.out.println("=====================================");
        Scanner sc = new Scanner(System.in);
        System.out.print("Para usar este conversor, é necessário uma chave de API do exchangerate: ");
        this.setChaveAPI(sc.nextLine().trim());
        String op;
        while(true)
        {
            try {
                System.out.println("Moeda de origem");
                menuMoeda();
                op = sc.nextLine();
                origem = escolhaMenu(op);

                System.out.println("Moeda de destino");
                menuMoeda();
                op = sc.nextLine();
                destino = escolhaMenu(op);
                System.out.print("Valor a ser convertido: $");
                Double valor = sc.nextDouble();

                System.out.println("=====================================");
                System.out.println(String.format("Valor convertido de %s para %s é $%.2f", origem, destino, converterMoeda(valor)));
                System.out.println("=====================================");
            } catch (Exception e) {
                System.out.println(e.getMessage());
                continue;
            }
        }
    }
    

    public static String escolhaMenu(String op) throws Exception
    {
        String origem="";
        switch (op) {
            case "1":
                origem = "BRL";
                break;

            case "2":
                origem = "USD";
                break;

            case "3":
                origem = "EUR";
                break;

            case "4":
                origem = "GBP";
                break;

            case "5":
                origem = "JPY";
                break;
            
            case "6":
                System.out.println("Saindo...");
                System.exit(0);
                break;
        
            default:
                throw new Exception("Opção Inválida");
        }
        return origem;
    }


    public Double converterMoeda(Double valor) throws Exception
    {
        System.out.println("Convertendo, aguarde um pouco...");
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s", chaveAPI, origem, destino)))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if(response.statusCode()!=200)
            throw new Exception("Erro em obter da API os dados");

        Gson gson = new Gson();
        ApiResponse get = gson.fromJson(response.body(), ApiResponse.class);
        return get.getConversionRate()*valor;

    }
}
