package com.praiseapp.comm;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService extends CommonService {
	
	@Autowired
	private JavaMailSender mailSender;
 
	public Map sendMail(Map<String, Object> paramMap) {
		Map rRtnData = new HashMap<String, String>();
	
		String sFrom		= ObjectUtils.toString(paramMap.get("from"));		// 보내는이 주소
		String sFromNm		= ObjectUtils.toString(paramMap.get("fromNm"));		// 보내는이 이름
		String sTo			= ObjectUtils.toString(paramMap.get("to"));			// 받는이 주소
		String sTitle		= ObjectUtils.toString(paramMap.get("title"));		// 메일제목
		String sContents	= ObjectUtils.toString(paramMap.get("contents"));	// 메일내용
		
		try {
			MimeMessage message = mailSender.createMimeMessage();
			MimeMessageHelper messageHelper;
			
			messageHelper = new MimeMessageHelper(message, true, "UTF-8");
			messageHelper.setFrom(sFrom, sFromNm);
			messageHelper.setTo(sTo);
			messageHelper.setSubject(sTitle);
			messageHelper.setText(sContents);
			mailSender.send(message);
			
		} catch (MailSendException e) {
			throw new BusinessException("메일발송 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.");
		} catch (MessagingException e) {
			throw new BusinessException("메일발송 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.");
		} catch (UnsupportedEncodingException e) {
			throw new BusinessException("메일발송 중 오류가 발생했습니다.\n잠시 후 다시 시도해주세요.");
		}
		
		rRtnData.put("status", 1);
		
		return rRtnData;
	}
}