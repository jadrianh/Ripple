package org.ripple.entities;

public class PhoneNumber {
    private String phoneSuffix;
    private String phoneNumber;
    private int idPhone;

    public PhoneNumber(String phoneSuffix, String phoneNumber, int idPhone) {
        this.phoneSuffix = phoneSuffix;
        this.phoneNumber = phoneNumber;
        this.idPhone = idPhone;
    }

    public String getPhoneSuffix() {
        return phoneSuffix;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public int getIdPhone() {
        return idPhone;
    }

    // Gracias a esto se pueden leer los numeros telefonicos, si te toca esta clase solo hace que en lugar de ser un array, los tome de la base de datos
    //PD PHoneTypes no funciona mucho dentro de la tabla PhoneNumber en la DB
    public static PhoneNumber[] getSamplePhoneNumbers() {
        return new PhoneNumber[]{
                new PhoneNumber("+503", "7519-2524", 1),
                new PhoneNumber("+503", "1234-5678", 2)
                // Agregar más números de teléfono según sea necesario
        };
    }
}
