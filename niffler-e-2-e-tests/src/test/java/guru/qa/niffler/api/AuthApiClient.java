package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import retrofit2.Response;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AuthApiClient extends RestClient {
    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
        this.authApi = retrofit.create(AuthApi.class);
    }

    public void requestRegisterForm() {
        final Response<Void> response;
        try {
            response = authApi.requestRegisterForm().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(200, response.code());
    }

    public void registerUser(String username, String password, String passwordSubmit, String csrf) {
        final Response<Void> response;
        try {
            response = authApi.register(username, password, passwordSubmit, csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertEquals(201, response.code());
    }
}
