package DiversityBenchmark.utils;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;

/**
 * 
 * @author Diversity
 * Contains the validators for the numbers
 *
 */
public class NumericValidator implements IValidator {
	private String integerPattern = "\\d*";
	private String doublePattern = "^[-+]?\\d+(\\.{0,1}(\\d+?))?$";

	@Override
	public IStatus validate(Object value) {
		if (value == null) {
			return ValidationStatus.ok();
		}
		if (value instanceof String) {
			String text = (String) value;
			if (text.trim().isEmpty() || text.matches(doublePattern)) {
				return ValidationStatus.ok();
			}
		}
		if (value instanceof Integer) {
			String s = String.valueOf(value);
			if (s.matches(doublePattern)) {
				return ValidationStatus.ok();
			}
		} else if (value instanceof Double) {
			String s = String.valueOf(value);
			if (s.matches(doublePattern)) {
				return ValidationStatus.ok();
			}
		}
		return ValidationStatus.error("Not a number");
	}
}
