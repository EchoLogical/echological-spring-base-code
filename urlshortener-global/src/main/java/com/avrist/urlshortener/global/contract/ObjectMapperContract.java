package com.avrist.urlshortener.global.contract;

import java.util.List;

public interface ObjectMapperContract<I, O> {
    O map(I input);
    List<O> mapList(List<I> input);
}
