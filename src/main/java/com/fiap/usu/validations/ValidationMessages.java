package com.fiap.usu.validations;

/**
 * Classe utilitária para centralizar as chaves de mensagens de validação.
 * Use estas constantes em vez de escrever a chave diretamente no código.
 */
public final class ValidationMessages {

    // ---------------- Usuário ----------------
    public static final String USER_NOT_FOUND = "validation.user.notfound";
    public static final String EMAIL_NOT_BLANK = "validation.email.notblank";
    public static final String EMAIL_INVALID = "validation.email.invalid";
    public static final String EMAIL_EXISTS = "validation.email.exists";
    public static final String LOGIN_EXISTS = "validation.login.exists";
    public static final String DOCUMENT_EXISTS = "validation.document.exists";
    public static final String PASSWORD_INVALID = "validation.password.invalid";
    public static final String CURRENT_PASSWORD_NOT_BLANK = "validation.currentPassword.notblank";
    public static final String CURRENT_PASSWORD_INVALID = "validation.currentPassword.invalid";
    public static final String NEW_PASSWORD_NOT_BLANK = "validation.newPassword.notblank";
    public static final String NEW_PASSWORD_SIZE = "validation.newPassword.size";

    // ---------------- Endereço ----------------
    public static final String ADDRESS_CEP_INVALID = "validation.address.cep.invalid";
    public static final String ADDRESS_STREET_INVALID = "validation.address.street.invalid";
    public static final String ADDRESS_CITY_INVALID = "validation.address.city.invalid";
    public static final String ADDRESS_STATE_INVALID = "validation.address.state.invalid";
    public static final String ADDRESS_NUMBER_INVALID = "validation.address.number.invalid";
    public static final String ADDRESS_PRIMARY_REQUIRED = "validation.address.primary.required";
    public static final String ADDRESS_PRIMARY_ONLY_ONE = "validation.address.primary.only.one";

    // ---------------- Autenticação ----------------
    public static final String INVALID_CREDENTIALS = "auth.invalid.credentials";
    public static final String NOT_AUTHENTICATED = "auth.not.authenticated";
    public static final String NO_ACCESS = "auth.no.access";

    // ---------------- Erros gerais ----------------
    public static final String FORBIDDEN = "error.forbidden";
    public static final String RESOURCE_NOT_FOUND = "error.resource.notfound";
    public static final String METHOD_NOT_ALLOWED = "error.method.not.allowed";

    private ValidationMessages() {
        // impede instanciação
    }
}
