package com.example.model;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.service.KeluargaService;
import com.example.service.PendudukService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PendudukModel
{
	private String id;
	private String nik;
	private String old_nik;
	private String nama;
	private String tempat_lahir;
	private String tanggal_lahir;
	private String kewarganegaraan;
	private int jenis_kelamin;
	private int is_wni;
	private int id_keluarga;
	private String agama;
	private String pekerjaan;
	private String status_perkawinan;
	private String status_dalam_keluarga;
	private String golongan_darah;
	private String wafat;
	private int is_wafat;
	private String alamat;
	private String rt;
	private String rw;
	private String nama_kota;
	private String nama_kecamatan;
	private String nama_kelurahan;
	private List<PendudukModel> same_penduduk;
	
	public void generateNik(KeluargaService keluargaDAO, PendudukService pendudukDAO) {
		String[] tanggalLahirTemp = tanggal_lahir.split("-");
		String tanggal = tanggalLahirTemp[2];
		String bulan = tanggalLahirTemp[1];
		String tahun = tanggalLahirTemp[0].substring(2, 4);
		//int bulan = Integer.parseInt(tanggalLahirTemp[1]);
		//int tahun = Integer.parseInt(tanggalLahirTemp[0]);
		
		KeluargaModel keluarga = keluargaDAO.getKeluarga(this.getId_keluarga());
		
		if(this.getJenis_kelamin() == 1) {
			tanggal = Integer.parseInt(tanggal) + 40 + "";
		}
		long nik = Long.parseLong(keluarga.getKode_kecamatan().substring(0,6) + tanggal + bulan + tahun + "0001");
		
		while(true) {
			PendudukModel checker = pendudukDAO.selectPenduduk(""+nik);
			if(checker != null) {
				nik++;
			}else {
				break;
			}
		}
		this.setNik(""+nik);
	}
}
