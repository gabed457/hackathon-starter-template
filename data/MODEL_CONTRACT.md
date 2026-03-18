# Model Contract

## Input

- Name: `input`
- Type: float32
- Shape: [1, 1] (one sample, one feature)
- Description: A score value between 0 and 1

## Output

- Name: `label`
- Type: int64
- Values: 0 = "low", 1 = "high"

## Example

- Input: [0.9] → Output: 1 (high)
- Input: [0.3] → Output: 0 (low)
