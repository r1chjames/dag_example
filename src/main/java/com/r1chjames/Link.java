package com.r1chjames;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Link {

    private Node source;
    private Node destination;
}
