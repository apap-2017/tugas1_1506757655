package com.example.service;

import java.util.List;

import com.example.model.PendudukModel;
public interface PendudukService
{
    PendudukModel selectPenduduk (String nik);
    void addPenduduk (PendudukModel penduduk);
    List<PendudukModel> selectAllPenduduk(String tanggal_lahir, String nama_kota, String nama_kecamatan, String nik);
    void updatePenduduk (PendudukModel penduduk);
    void updateStatusKematian (PendudukModel penduduk);
    List<PendudukModel> selectAllPendudukKelurahan (String id_kelurahan);
}
