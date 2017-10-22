package com.example.service;

import java.util.List;

import com.example.model.KecamatanModel;
import com.example.model.KelurahanModel;
import com.example.model.KotaModel;
import com.example.model.PendudukModel;

public interface LokasiService {
	List<KelurahanModel> selectAllKelurahan();
	List<KecamatanModel> selectAllKecamatan();
	List<KotaModel> selectAllKota();
	KelurahanModel selectKelurahan(int id);
	List<KecamatanModel> selectAllKecamatanByKota(String id_kota);
	KotaModel selectKotaById(String id_kota);
	KecamatanModel selectKecamatanById(String id_kecamatan);
	KelurahanModel selectKelurahanById(String id_kelurahan);
	List<KelurahanModel> selectAllKelurahanByKecamatan(String id_kecamatan);
}
