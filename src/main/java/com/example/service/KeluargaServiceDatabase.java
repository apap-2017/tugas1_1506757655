package com.example.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dao.KeluargaMapper;
import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KeluargaServiceDatabase implements KeluargaService
{
    @Autowired
    private KeluargaMapper keluargaMapper;

    @Override
	 public List<PendudukModel> selectPenduduks(String nkk)
	 {
		 log.info("select penduduk with nkk");
		 return keluargaMapper.selectPenduduks(nkk);
	 }

	@Override
	 public KeluargaModel selectKeluarga (String nkk)
	 {
		 log.info("select keluarga with nkk");
		 return keluargaMapper.selectKeluarga(nkk);
	 }
	
	@Override
	 public KeluargaModel getKeluarga (int id)
	 {
		 log.info("select keluarga with nkk");
		 return keluargaMapper.getKeluarga(id);
	 }
	
	@Override
	public List<KeluargaModel> selectAllKeluarga(String nama_kota, String nama_kecamatan, String nkk) {
		log.info("List dari keluarga dari kota {} kecamatan {}", nama_kota, nama_kecamatan);
		return keluargaMapper.selectAllKeluarga(nama_kota, nama_kecamatan, nkk);
	}
	
	@Override
	 public void addKeluarga (KeluargaModel keluarga)
	 {
		 log.info("add keluarga");
		 keluargaMapper.addKeluarga(keluarga);
	 }	 
	
	@Override
	 public void updateKeluarga (KeluargaModel keluarga){
		 log.info("update keluarga");
		 keluargaMapper.updateKeluarga(keluarga);
	 }
    
}
