package engine.vv.model;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@Slf4j
public class IntegerListConverter implements AttributeConverter<List<Integer>, String> {
    private static final String SPLIT_CHAR = ";";

    @Override
    public String convertToDatabaseColumn(List<Integer> integerList) {
        String result = integerList != null ?
                String.join(SPLIT_CHAR, integerList.stream()
                        .map(intg -> intg == null
                                ? ""
                                : intg.toString())
                        .collect(Collectors.toList()))
                : "";
        //log.info("VV64: convertToDatabaseColumn: {} from {}", result, integerList);
        return result;
    }

    @Override
    public List<Integer> convertToEntityAttribute(String string) {
        List<Integer> result = string != null ?
                Arrays.asList(string.split(SPLIT_CHAR)).stream()
                        .map(a -> a.isBlank() ? null
                                : Integer.parseInt(a))
                        .collect(Collectors.toList())
                : emptyList();
        if (result.size() == 1 && result.get(0) == null) {
            result = emptyList();
        }
        //log.info("VV64: convertToEntityAttribute: {} from {}", result, string);
        return result;
    }
}
