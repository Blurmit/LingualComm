package me.blurmit.lingualcomm.request;

import java.util.concurrent.CompletableFuture;

public interface IRequest {

    String send();

    default CompletableFuture<String> sendAsync() {
        return CompletableFuture.supplyAsync(this::send);
    }

}
