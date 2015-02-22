package repository.normalizers;

import model.Radiography;

import java.util.List;

public interface RadiographyValuesNormalizer {
    public void normalize(List<Radiography> radiographyList);
}
