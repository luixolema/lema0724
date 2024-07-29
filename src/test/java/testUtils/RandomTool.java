package testUtils;

import com.lema.test.entities.Tool;
import com.lema.test.entities.ToolType;

public class RandomTool {

    public static Tool randomTool() {
        Tool tool = new Tool();
        tool.setToolCode(ValueProvider.randomString(""));
        tool.setToolType(randomToolType());
        tool.setId(ValueProvider.randomId());
        tool.setBrand(ValueProvider.randomString(""));
        return tool;
    }

    public static ToolType randomToolType() {
        ToolType toolType = new ToolType();
        toolType.setId(ValueProvider.randomId());
        toolType.setName(ValueProvider.randomString(""));
        toolType.setDailyCharge(ValueProvider.randomBigDecimal());
        toolType.setWeekdayCharge(ValueProvider.randomBoolean());
        toolType.setWeekendCharge(ValueProvider.randomBoolean());
        toolType.setHolidayCharge(ValueProvider.randomBoolean());

        return toolType;
    }
}
