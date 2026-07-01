package com.tour.app.auth.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.tour.app.auth.repository.AuthRepository;
import com.tour.app.common.BaseViewModel;
import com.tour.app.model.User;
import com.tour.app.network.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthViewModel extends BaseViewModel {
    private final AuthRepository repository;
    private final MutableLiveData<Boolean> loginResult = new MutableLiveData<>();
    private final MutableLiveData<String> authToken = new MutableLiveData<>();
    private final MutableLiveData<Boolean> registerResult = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    public AuthViewModel(AuthRepository repository) {
        this.repository = repository;
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final AuthRepository repository;

        public Factory(AuthRepository repository) {
            this.repository = repository;
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            if (modelClass.isAssignableFrom(AuthViewModel.class)) {
                return (T) new AuthViewModel(repository);
            }
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }

    public MutableLiveData<Boolean> getLoginResult() {
        return loginResult;
    }

    public MutableLiveData<String> getAuthToken() {
        return authToken;
    }

    public MutableLiveData<Boolean> getRegisterResult() {
        return registerResult;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void login(String username, String password) {
        isLoading.setValue(true);
        repository.login(username, password)
                .enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<String> apiResponse = response.body();
                            if (apiResponse.getCode() == 200) {
                                String token = apiResponse.getData();
                                if (token != null && !token.isEmpty()) {
                                    authToken.setValue(token);
                                    loginResult.setValue(true);
                                    errorMessage.setValue(null);
                                } else {
                                    loginResult.setValue(false);
                                    errorMessage.setValue("token为空");
                                }
                            } else {
                                loginResult.setValue(false);
                                errorMessage.setValue(apiResponse.getMessage());
                            }
                        } else {
                            loginResult.setValue(false);
                            errorMessage.setValue("登录失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                        isLoading.setValue(false);
                        loginResult.setValue(false);
                        errorMessage.setValue("网络错误: " + t.getMessage());
                    }
                });
    }

    public void register(String username, String password, String phone, String email, String nickname) {
        isLoading.setValue(true);
        repository.register(username, password, phone, email, nickname)
                .enqueue(new Callback<ApiResponse<String>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                        isLoading.setValue(false);
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<String> apiResponse = response.body();
                            if (apiResponse.getCode() == 200) {
                                registerResult.setValue(true);
                                errorMessage.setValue(null);
                            } else {
                                registerResult.setValue(false);
                                errorMessage.setValue(apiResponse.getMessage());
                            }
                        } else {
                            registerResult.setValue(false);
                            errorMessage.setValue("注册失败");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                        isLoading.setValue(false);
                        registerResult.setValue(false);
                        errorMessage.setValue("网络错误: " + t.getMessage());
                    }
                });
    }
}
