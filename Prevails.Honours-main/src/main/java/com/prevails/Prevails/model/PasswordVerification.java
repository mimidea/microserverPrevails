package com.prevails.Prevails.model;

public interface PasswordVerification {

	default int isPasswordValid (String password) {
		boolean length = false;
		boolean upper = false;
		boolean lower = false;
		boolean numeric = false;
		boolean symbol = false;
		int lengthInt = password.length();
		char[] passwordArray = password.toCharArray();
		System.out.println(password);
		if (lengthInt > 6 && lengthInt < 20) {
			length = true;
		}
		
		for (int character = 0; character < passwordArray.length; character++) {
			if (passwordArray[character] <= 90 && passwordArray[character] >= 65) {
				upper = true;
			} else if (passwordArray[character] <= 122 && passwordArray[character] >= 97) {
				lower = true;
			} else if (passwordArray[character] <= 57 && passwordArray[character] >= 48) {
				numeric = true;
			} else if (Integer.valueOf(passwordArray[character]) <= 47 && Integer.valueOf(passwordArray[character]) >= 33) {
				symbol = true;
			} else if (passwordArray[character] <= 64 && passwordArray[character] >= 58) {
				symbol = true;
			} else if (passwordArray[character] <= 96 && passwordArray[character] >= 91) {
				symbol = true;
			} else if (passwordArray[character] <= 126 && passwordArray[character] >= 123) {
				symbol = true;
			}
		}
		if (length && upper && lower && numeric && symbol) {
			return 200;
		} else if (!length) {
			return 1;
		} else if (!upper) {
			return 2;
		} else if (!lower) {
			return 3;
		} else if (!numeric) {
			return 4;
		}else if (!symbol) {
			return 5;
		} else 
			return 0;
		
	}
	
}
