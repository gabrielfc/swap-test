package br.com.swaptest.domain;


import java.util.List;

public record Issue (String title, String author, List<String> labels){

}
