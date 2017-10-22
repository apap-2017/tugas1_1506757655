package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KeluargaMapper;
import com.example.dao.PendudukMapper;
import com.example.model.PendudukModel;
import com.example.model.KeluargaModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PendudukServiceDatabase implements PendudukService
{
    @Autowired
    private PendudukMapper pendudukMapper;
    
    @Autowired
	private KeluargaMapper keluargaMapper;


    @Override
    public PendudukModel selectPenduduk (String nik)
    {
        log.info ("select penduduk with nik {}", nik);
        return pendudukMapper.selectPenduduk (nik);
    }

    
    @Override
    public void addPenduduk (PendudukModel penduduk)
    {
        pendudukMapper.addPenduduk (penduduk);
    }
    
    @Override
    public void updatePenduduk (PendudukModel penduduk)
    {
    	log.info("penduduk updated");
    	pendudukMapper.updatePenduduk (penduduk);
    }
    
    @Override
	public List<PendudukModel> selectAllPenduduk(String tanggal_lahir, String nama_kota, String nama_kecamatan, String nik) {
		log.info("List dari penduduk dengan tanggal lahir {} dari kota {} kecamatan {}", tanggal_lahir, nama_kota, nama_kecamatan);
		return pendudukMapper.selectAllPenduduk(tanggal_lahir, nama_kota, nama_kecamatan, nik);
	}
    
    @Override
	 public void updateStatusKematian (PendudukModel penduduk)
	 {
		 log.info("update status kematian penduduk");
		 int id_keluarga = penduduk.getId_keluarga();
		 List<PendudukModel> anggota = pendudukMapper.selectAnggotaKeluarga(id_keluarga);
		 
		 int wafat = 0;
		 for (int i = 0; i < anggota.size(); i++) {
			 if(anggota.get(i).getIs_wafat() == 1) {
				 wafat++;
			 }
		 }
		 
		 if(wafat == anggota.size()) {
			
			keluargaMapper.updateStatusKematian(id_keluarga);
		 }
		 pendudukMapper.updateStatusKematian(penduduk.getNik());
	 }
    
    @Override
	public List<PendudukModel> selectAllPendudukKelurahan(String id_kelurahan) {
		log.info("select penduduk dari kelurahan dengan id {}", id_kelurahan);
		return pendudukMapper.selectAllPendudukKelurahan(id_kelurahan);
	}
}
