package util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Range {
    public long min, max;

    public Range intersect(Range other) {
        if (this.overlaps(other)) {
            return new Range(Math.max(this.min, other.min), Math.min(this.max, other.max));
        }

        return null;
    }

    public boolean overlaps(Range other) {
        return this.min <= other.max && this.max >= other.min;
    }

    public Range exclude(Range other) {
        if (!this.overlaps(other)) {
            return this;
        }

        if (other.min <= this.min) {
            // exclude all
            if (other.max >= this.max) {
                return null;
            } else {
                return new Range(other.max + 1, this.max);
            }
        } else if (other.max >= this.max) {
            return new Range(this.min, other.min - 1);
        } else {
            return null;
        }
    }

    /**
     * We assume that we already checked 2 ranges are overlap. Otherwise, this returns wrong result
     */
    public Range merge(Range other) {
        return new Range(Math.min(this.min, other.min), Math.max(this.max, other.max));
    }

    public long length() {
        return max - min + 1;
    }

}
