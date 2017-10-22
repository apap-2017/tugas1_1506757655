package com.example.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.model.KeluargaModel;
import com.example.model.KelurahanModel;
import com.example.model.PendudukModel;
import com.example.service.LokasiService;
import com.example.service.KeluargaService;
import com.example.service.PendudukService;

@Controller
public class KeluargaController {
	
	@Autowired
    PendudukService pendudukDAO;
	
	@Autowired
    KeluargaService keluargaDAO;
	
	@Autowired
    LokasiService lokasiDAO;
	
	@RequestMapping("/keluarga/tambah")
    public String addKeluarga (Model model)
    {
		model.addAttribute("keluarga", new KeluargaModel());
		model.addAttribute("allKelurahan", lokasiDAO.selectAllKelurahan());
		model.addAttribute("allKecamatan", lokasiDAO.selectAllKecamatan());
		model.addAttribute("allKota", lokasiDAO.selectAllKota());
		return "add-keluarga";
    }


    @RequestMapping(value = "/keluarga/tambah/submit", method = RequestMethod.POST)
    public String addKeluargaSubmit ( Model model, @Valid @ModelAttribute KeluargaModel keluarga, BindingResult result)
    {	
    	if(result.hasErrors()) {
    		return "error-page";
    	}
    	
    	LocalDate date = LocalDate.now();
    	String localdate = DateTimeFormatter.ofPattern("ddMMyy").format(date);
    	
    	KelurahanModel kelurahan = lokasiDAO.selectKelurahan(Integer.parseInt(keluarga.getId_kelurahan()));
    	
    	String nkk = kelurahan.getKode_kelurahan().substring(0, 6) + localdate + "0001";
    	
    	List<KeluargaModel> listKeluarga = keluargaDAO.selectAllKeluarga(keluarga.getId_kota(), keluarga.getId_kecamatan(), nkk.substring(0,  11));
    	
    	Long nkk_baru = Long.parseLong(nkk);
        
        while(true) {
         KeluargaModel keluargaCheck = keluargaDAO.selectKeluarga (nkk_baru + "");
         if(keluargaCheck != null) {
          nkk_baru++;
         } else {
          break;
         }
        }
    	
    	keluarga.setNomor_kk(nkk_baru + "");
    	
    	keluargaDAO.addKeluarga(keluarga);
    	model.addAttribute ("nkk", keluarga.getNomor_kk());
        return "success-add-keluarga";    		
    }
    
    @RequestMapping("/keluarga")
    public String viewKeluarga (Model model,
            @RequestParam(value = "nkk") String nkk)
    {
       KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);
        if (keluarga != null) {
            model.addAttribute ("keluarga", keluarga);
            return "viewKeluarga";
        } else {
            return "error-page";
        }
    }
    
    @RequestMapping("/keluarga/ubah/{nkk}")
    public String ubahPenduduk(Model model, 
            @PathVariable(value = "nkk") String nkk)
    {
		KeluargaModel keluarga = keluargaDAO.selectKeluarga(nkk);
		model.addAttribute("allKelurahan", lokasiDAO.selectAllKelurahan());
		model.addAttribute("keluarga", keluarga);
        return "update-keluarga";
    }
	

	@RequestMapping(value = "/keluarga/ubah/{nkk}", method = RequestMethod.POST)
    public String ubahKeluarga(Model model, @PathVariable(value = "nkk") String nkk,
    		@ModelAttribute KeluargaModel keluarga)
    {
		KeluargaModel keluargaLama = keluargaDAO.selectKeluarga(nkk);
		keluarga.setNomor_kk(keluargaLama.getNomor_kk());
		keluarga.setId(keluargaLama.getId());
		keluarga.setIs_tidak_berlaku(keluargaLama.getIs_tidak_berlaku());

		if(!keluargaLama.getId_kelurahan().equals(keluarga.getId_kelurahan())) {
			keluarga.generateNkk(keluargaDAO);
		}
		keluargaDAO.updateKeluarga(keluarga);
		
		model.addAttribute("nkk", keluarga.getNomor_kk());
        return "success-add-keluarga";
    }

}
