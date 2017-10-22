package com.example.controller;

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

import com.example.model.KecamatanModel;
import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.service.PendudukService;
import com.example.service.KeluargaService;
import com.example.service.LokasiService;

@Controller
public class PendudukController {
	@Autowired
	PendudukService pendudukDAO;
	
	@Autowired
	KeluargaService keluargaDAO;

	@Autowired
	LokasiService lokasiDAO;
	
	@RequestMapping("/")
    public String index ()
    {
        return "index";
    }

	@RequestMapping("/penduduk")
    public String viewPenduduk (Model model,
            @RequestParam(value = "nik") String nik)
    {
       PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
        if (penduduk != null) {
        	if(penduduk.getIs_wni() == 1) {
        		penduduk.setKewarganegaraan("WNI");
        	} else {
        		penduduk.setKewarganegaraan("WNA");
        	}
        	
        	if (penduduk.getIs_wafat() == 0) {
        		penduduk.setWafat("Hidup");
        	}else {
        		penduduk.setWafat("Wafat");
        	}
            model.addAttribute ("penduduk", penduduk);
            return "viewPenduduk";
        } else {
            return "error-page";
        }
    }
	
	@RequestMapping("/penduduk/tambah")
    public String tambahPenduduk(Model model)
    {
		PendudukModel penduduk = new PendudukModel();
		model.addAttribute("penduduk", penduduk);
        return "add-penduduk";
    }
	

	@RequestMapping(value = "/penduduk/tambah", method = RequestMethod.POST)
    public String tambahPenduduk(Model model,@Valid @ModelAttribute PendudukModel penduduk)
    {
		penduduk.generateNik(keluargaDAO, pendudukDAO);
		pendudukDAO.addPenduduk(penduduk);
		model.addAttribute("nik", penduduk.getNik());
        return "success-add-penduduk";
    }
	
	@RequestMapping("/penduduk/ubah/{nik}")
    public String updatePenduduk (Model model, @PathVariable(value = "nik") String nik)
    {
        if(pendudukDAO.selectPenduduk(nik) != null) {
        	PendudukModel penduduk = pendudukDAO.selectPenduduk (nik);
        	
        	if(penduduk != null) {
        		model.addAttribute ("penduduk", penduduk);
        		return "update-penduduk";
        	}
        }
        model.addAttribute ("nik", nik);
        return "error-page";
    }
    
    @RequestMapping(value = "/penduduk/ubah/{nik}", method = RequestMethod.POST)
    public String updatePendudukSubmit (Model model, @ModelAttribute PendudukModel penduduk)
    {	
    	String nik_lama = penduduk.getNik();
        String tanggalTemp[] = penduduk.getTanggal_lahir().split("-");
        String nik_tanggal = tanggalTemp[2] +  tanggalTemp[1] +  tanggalTemp[0].substring(2, 4);
        
        if(!nik_lama.substring(6, 12).equals(nik_tanggal)) {
         nik_tanggal = (nik_lama.substring(0, 6) + nik_tanggal + "0001");
        } else {
         nik_tanggal = nik_lama;
        }
        
        Long nik_baru = Long.parseLong(nik_tanggal);
        
        while(true) {
         PendudukModel pendudukCheck = pendudukDAO.selectPenduduk (nik_baru + "");
         if(pendudukCheck != null) {
          nik_baru++;
         } else {
          break;
         }
        }

        penduduk.setNik(nik_baru + "");
        pendudukDAO.updatePenduduk (penduduk);
        
        model.addAttribute ("nik", nik_lama);
           return "success-update-penduduk";		
    }
    
    @RequestMapping(value = "/penduduk/mati", method = RequestMethod.POST)
    public String statusKematian(Model model, @RequestParam(value = "nik") String nik)
    {
		PendudukModel penduduk = pendudukDAO.selectPenduduk(nik);
		pendudukDAO.updateStatusKematian(penduduk);

		model.addAttribute("nik", nik);
        return "success-update-death";
    }
	
    @RequestMapping("/penduduk/cari")
    public String cariPenduduk (Model model,
            @RequestParam(value = "kt", required = false) String kota,
            @RequestParam(value = "kc", required = false) String kecamatan,
            @RequestParam(value = "kl", required = false) String kelurahan)
    {	
    	if(kota == null) {
	    	List<KotaModel> listKota = lokasiDAO.selectAllKota();
	    	model.addAttribute ("kota", listKota);
	        return "cari-penduduk-kota";
    	} else {
    		if(kecamatan == null) {
    			KotaModel kotaObj = lokasiDAO.selectKotaById(kota);
    			List<KecamatanModel> listKecamatan = lokasiDAO.selectAllKecamatan();
    	    	
    			System.out.println(kotaObj.toString());
    			
    	    	model.addAttribute ("kota", kotaObj);
    	    	model.addAttribute ("kecamatan", listKecamatan);
    	        return "cari-penduduk-kecamatan";
        	} else {
        		if(kelurahan == null) {
        			KotaModel kotaObj = lokasiDAO.selectKotaById(kota);
        			KecamatanModel kecamatanObj = lokasiDAO.selectKecamatanById(kecamatan);
        			List<KelurahanModel> listKelurahan = lokasiDAO.selectAllKelurahan();
        	    	
        	    	model.addAttribute ("kota", kotaObj);
        	    	model.addAttribute ("kecamatan", kecamatanObj);
        	    	model.addAttribute ("kelurahan", listKelurahan);
        	        return "cari-penduduk-kelurahan";
            	} 
        	}
    	}
    	KotaModel kotaObj = lokasiDAO.selectKotaById(kota);
		KecamatanModel kecamatanObj = lokasiDAO.selectKecamatanById(kecamatan);
    	KelurahanModel kelurahanObj = lokasiDAO.selectKelurahanById(kelurahan);
		
    	List<PendudukModel> pendudukKelurahan = pendudukDAO.selectAllPendudukKelurahan(kelurahan);
    	
    	for(int i = 0; i < pendudukKelurahan.size(); i++) {
    		pendudukKelurahan.get(i).setJenis_kelamin(pendudukKelurahan.get(i).getJenis_kelamin());
    	}
    	
    	PendudukModel pendudukTermuda = pendudukKelurahan.get(0);
    	PendudukModel pendudukTertua = pendudukKelurahan.get(0);
    	
    	for(int i = 0; i < pendudukKelurahan.size(); i++) {
    		if(pendudukTermuda.getTanggal_lahir().compareTo(pendudukKelurahan.get(i).getTanggal_lahir()) > 0) {
    			pendudukTermuda = pendudukKelurahan.get(i);
    		}
    		if(pendudukTertua.getTanggal_lahir().compareTo(pendudukKelurahan.get(i).getTanggal_lahir()) <= 0) {
    			pendudukTertua = pendudukKelurahan.get(i);
    		}
    	}
    	
    	model.addAttribute ("pendudukTertua", pendudukTertua);
    	model.addAttribute ("pendudukTermuda", pendudukTermuda);
    	model.addAttribute ("kota", kotaObj);
    	model.addAttribute ("kecamatan", kecamatanObj);
    	model.addAttribute ("kelurahan", kelurahanObj);
    	model.addAttribute ("penduduk", pendudukKelurahan);
    	return "cari-result";
    }
}
