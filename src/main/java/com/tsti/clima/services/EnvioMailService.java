package com.tsti.clima.services;

import org.springframework.stereotype.Service;

@Service
public class EnvioMailService {

	/**
	 * función que emula el envío de mail, imprime en consóla la dirección de mail del destinatario y el cuerpo del mensaje a enviar. 
	 * @param mail
	 * @param cuerpo
	 */
	public void enviarMail(String mail, String cuerpo) {
		System.out.println(mail + "\n" + cuerpo);
	}
}
