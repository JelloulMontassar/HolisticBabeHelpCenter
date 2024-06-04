package com.example.holisticbabehelpcenter.service;

import com.example.holisticbabehelpcenter.exceptions.ResourceNotFoundException;
import com.example.holisticbabehelpcenter.model.Faq;
import com.example.holisticbabehelpcenter.repository.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FaqService {

    @Autowired
    private FaqRepository faqRepository;

    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    public Faq getFaqById(Long id) {
        return faqRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("FAQ not found"));
    }

    public Faq createFaq(Faq faq) {
        return faqRepository.save(faq);
    }

    public Faq updateFaq(Long id, Faq faqDetails) {
        Faq faq = faqRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("FAQ not found"));
        faq.setQuestion(faqDetails.getQuestion());
        faq.setAnswer(faqDetails.getAnswer());
        return faqRepository.save(faq);
    }

    public void deleteFaq(Long id) {
        Faq faq = faqRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("FAQ not found"));
        faqRepository.delete(faq);
    }
}

