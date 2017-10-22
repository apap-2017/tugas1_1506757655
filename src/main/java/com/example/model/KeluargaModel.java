package com.example.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.service.KeluargaService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel {
	 private String id;
	 private String nomor_kk;
	 private String old_nomor_kk;
	 private String alamat;
	 @NotNull
	 @Size(min=1, max=3)
	 private String rt;
	 @NotNull
	 @Size(min=1, max=3)
	 private String rw;
	 private String id_kelurahan;
	 private String kode_kelurahan;
	 private int is_tidak_berlaku;
	 private String id_kecamatan;
	 private String id_kota;
	 private String kode_kecamatan;
	 private String nama_kecamatan;
	 private String nama_kelurahan;
	 private String nama_kota;
	 private String kode_pos;
	 private List<PendudukModel> penduduks;
	 
	 public void generateNkk(KeluargaService keluargaDAO) {
			StringTokenizer tanggalLahirTokenizer = new StringTokenizer(new SimpleDateFormat("yy-MM-dd").format(new Date()), "-");
			String tahun = tanggalLahirTokenizer.nextToken().substring(2);
			String bulan = tanggalLahirTokenizer.nextToken();
			String tanggal = tanggalLahirTokenizer.nextToken();
			
			long nkk = Long.parseLong(this.getKode_kecamatan().substring(0,6) + tanggal + bulan + tahun + "0001");
			
			while(true) {
				KeluargaModel checker = keluargaDAO.selectKeluarga(""+nkk);
				if(checker != null) {
					nkk++;
				}else {
					break;
				}
			}
			this.setNomor_kk(""+nkk);
		}
}
