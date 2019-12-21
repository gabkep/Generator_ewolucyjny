package projekt;

import java.io.FileReader;
import org.json.*;

public class MapProperties {
    private Integer width;
    private Integer height;
    private Integer startEnergy;
    private Integer moveEnergy;
    private Integer plantEnergy;
    private Double jungleRatio;

    public MapProperties(FileReader fileReader)
    {
        JSONObject jsonObject = new JSONObject(new JSONTokener(fileReader));
        this.width = (Integer) jsonObject.get("width");
        this.height = (Integer) jsonObject.get("height");
        this.startEnergy = (Integer) jsonObject.get("startEnergy");
        this.moveEnergy = (Integer) jsonObject.get("moveEnergy");
        this.plantEnergy = (Integer) jsonObject.get("plantEnergy");
        this.jungleRatio = (Double) jsonObject.get("jungleRatio");
    }

    public Integer getWidth() {
        return width;
    }

    public Integer getHeight() {
        return height;
    }

    public Integer getPlantEnergy() {
        return plantEnergy;
    }

    public Integer getStartEnergy() {
        return startEnergy;
    }

    public Integer getMoveEnergy() {
        return moveEnergy;
    }

    public Double getJungleRatio() {
        return jungleRatio;
    }
}
