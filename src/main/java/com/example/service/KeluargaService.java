package com.example.service;

import java.util.List;

import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

public interface KeluargaService {
	List<PendudukModel> selectPenduduks(String nkk);
	KeluargaModel selectKeluarga(String nkk);
	KeluargaModel getKeluarga(int id);
	List<KeluargaModel> selectAllKeluarga(String nama_kota, String nama_kecamatan, String nik);
	void addKeluarga(KeluargaModel keluarga);
	void updateKeluarga (KeluargaModel keluarga);
}
