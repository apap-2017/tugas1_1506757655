package com.example.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.example.model.KeluargaModel;
import com.example.model.PendudukModel;

@Mapper
public interface PendudukMapper {
 
	@Select("SELECT * FROM penduduk join keluarga join kelurahan join kecamatan join kota"
		 + " ON penduduk.id_keluarga = keluarga.id AND keluarga.id_kelurahan = kelurahan.id "
		 + " AND kelurahan.id_kecamatan = kecamatan.id AND kecamatan.id_kota = kota.id"
		 + " WHERE nik = #{nik}")
	PendudukModel selectPenduduk (@Param("nik") String nik);

 
 	@Insert("INSERT INTO penduduk (nik, nama, tempat_lahir, tanggal_lahir, jenis_kelamin, is_wni,"
		 + " id_keluarga, agama, pekerjaan, status_perkawinan, status_dalam_keluarga, "
		 + " golongan_darah, is_wafat)"
		 + " VALUES ('${nik}', '${nama}', '${tempat_lahir}', '${tanggal_lahir}', ${jenis_kelamin},"
		 + " ${is_wni}, '${id_keluarga}', '${agama}', '${pekerjaan}', '${status_perkawinan}', "
		 + " '${status_dalam_keluarga}', '${golongan_darah}',"
		 + " '${is_wafat}')")
 	void addPenduduk (PendudukModel penduduk);
 	
 	@Update("UPDATE PENDUDUK SET nik=#{nik}, nama=#{nama}, tempat_lahir=#{tempat_lahir}, tanggal_lahir=#{tanggal_lahir}, jenis_kelamin=#{jenis_kelamin}, is_wni=#{is_wni},"
 			+ "id_keluarga=#{id_keluarga}, agama=#{agama}, pekerjaan=#{pekerjaan}, status_perkawinan=#{status_perkawinan},"
 			+ "golongan_darah=#{golongan_darah} WHERE id=#{id}")
    void updatePenduduk (PendudukModel penduduk);
    
 	@Select("select distinct penduduk.nik, penduduk.nama, kota.kode_kota, kota.nama_kota, kecamatan.kode_kecamatan, "
    		+ "kecamatan.nama_kecamatan from penduduk join kota join kecamatan join kelurahan "
    		+ "on penduduk.tanggal_lahir = #{tanggal_lahir} and kota.nama_kota = #{nama_kota} and "
    		+ "kecamatan.nama_kecamatan = #{nama_kecamatan} where penduduk.nik LIKE CONCAT(#{nik}, '%')")
    List<PendudukModel> selectAllPenduduk (@Param("tanggal_lahir") String tanggal_lahir, @Param("nama_kota") String nama_kota, 
    		@Param("nama_kecamatan") String nama_kecamatan, @Param("nik") String nik);
 	
 	@Update("UPDATE penduduk SET is_wafat = 1 WHERE nik = #{nik}")
	void updateStatusKematian(@Param("nik") String nik);
 	
 	@Select("select * from keluarga, penduduk where penduduk.id_keluarga = keluarga.id and #{id_keluarga} = penduduk.id_keluarga")
    List<PendudukModel> selectAnggotaKeluarga(int id_keluarga);
 	
 	@Select("select * from penduduk join keluarga on penduduk.id_keluarga = keluarga.id where keluarga.id_kelurahan = #{id_kelurahan}")
    List<PendudukModel> selectAllPendudukKelurahan (@Param("id_kelurahan") String id_kelurahan);

 	
 	
}	