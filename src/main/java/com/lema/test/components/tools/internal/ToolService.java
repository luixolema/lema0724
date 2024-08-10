package com.lema.test.components.tools.internal;

import com.lema.test.components.tools.api.output.IToolService;
import com.lema.test.components.tools.api.output.ToolRentalData;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class ToolService implements IToolService {
    private final ToolRepository toolRepository;
    private final ToolTypeRepository toolTypeRepository;


    @Override
    public ToolRentalData getToolRentalData(String code) {
        Tool tool = this.toolRepository.findByToolCode(code).orElseThrow(() -> {
            String message = String.format("Tool with code: \"%s\" doesn't exist", code);
            log.error(message);
            return new EntityNotFoundException(message);
        });

        ToolType toolType = this.toolTypeRepository.findById(tool.getToolType().getId()).orElseThrow(() -> {
            String message = String.format("Tool type with id: \"%d\" doesn't exist", tool.getId());
            log.error(message);
            return new EntityNotFoundException(message);
        });

        return new ToolRentalData(
                toolType.getWeekendCharge(),
                toolType.getWeekdayCharge(),
                toolType.getHolidayCharge(),
                toolType.getDailyCharge(),
                tool.getToolCode(),
                tool.getBrand(),
                toolType.getName()
        );
    }
}
