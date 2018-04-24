package com.Project4;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.awt.Desktop;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DisplaySoupTypes();
        String soup = GetSoupType();
        String soupPage = GetURL(soup);

        if (soupPage.isEmpty()) {
            System.out.println("Please enter a valid soup type.");

            soup = GetSoupType();
            soupPage = GetURL(soup);
        }

        GetSoupImg(soupPage);
        WaitForEnter();
        GetNutritionImg(soupPage);
        WaitForEnter();
        GetIngredients(soupPage);

    }
    private static void GetIngredients(String soupURL) {
        String ingredients = "";

        try {
            Document doc = Jsoup.connect(soupURL).get();

            ingredients = "\nIngredients: " +  doc.getElementsByClass("product-nutritional-info ingredientes").select("p").first().text();

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(ingredients);


    }

    private static void GetSoupImg(String soup) {
        try {
            Document doc = Jsoup.connect("https://www.progresso.com/products/").get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                if (Desktop.isDesktopSupported() && link.attr("href").contains(soup)) {
                    Desktop.getDesktop().browse(new URI(link.child(0).attr("src")));
                }
            }
        } catch (IOException e){
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    private static void GetNutritionImg(String soupURL) {
        try {
            Document doc = Jsoup.connect(soupURL).get();

            Elements elements = doc.select("figure.nutritional-info-content > *");
            for (Element each : elements) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(new URI(each.attr("src")));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private static String GetSoupType(){
        Scanner in = new Scanner(System.in);

        System.out.println("\nPlease enter the type of soup you want, as shown: ");
        return in.nextLine();
    }
    private static void DisplaySoupTypes(){
        System.out.println("Welcome to Progresso soups!!!\n");

        try {
            Document doc = Jsoup.connect("https://www.progresso.com/products/").get();

            System.out.println("Soups:\n");
            Elements links = doc.select("a[href]");
            for (Element link : links) {
                if (link.attr("href").contains("?category=")) {
                    System.out.println(link.attr("href").split("[/|?]")[4]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String GetURL(String soupType){
        String soupURL = "";
        try {
            Document doc = Jsoup.connect("https://www.progresso.com/products/").get();

            Elements links = doc.select("a[href]");
            for (Element link : links) {
                if (link.attr("href").contains(soupType)){
                    soupURL = link.attr("href");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return soupURL;
    }
    private static void WaitForEnter(){
        System.out.println("\nPress enter to continue... ");
        try { System.in.read(); }
        catch (Exception e) { }
    }
}
