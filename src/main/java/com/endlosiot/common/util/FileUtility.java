/*******************************************************************************
 * Copyright -2019 @intentlabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.endlosiot.common.util;

import com.endlosiot.common.enums.ResponseCode;
import com.endlosiot.common.exception.EndlosiotAPIException;
import com.endlosiot.common.file.enums.ImageFileExtensionEnum;
import com.endlosiot.common.logger.LoggerService;
import com.endlosiot.common.setting.model.SystemSettingModel;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Base64;
import java.util.Iterator;

/**
 * This utility is used to perform all files related operations.
 *
 * @author Nirav.Shah
 * @since 27/12/2019
 */
public class FileUtility {

    private FileUtility() {
        // Not allow to create any object.
    }

    /**
     * This method is used to write uploaded file on server. It can create thumb
     * nail of uploaded image base on given height and size.
     *
     * @param multipartFile - file
     * @param filePath      - Path where uploaded file will be stored
     * @param originalName  - name of file.
     * @return
     * @throws EndlosiotAPIException
     */
    public static File upload(MultipartFile multipartFile, String filePath, String originalName)
            throws EndlosiotAPIException {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            byte[] bytes = multipartFile.getBytes();
            String uploadedFileName = originalName.substring(0, originalName.lastIndexOf("."));
            String ext = originalName.substring(originalName.lastIndexOf("."));

            File newFile = new File(
                    filePath + File.separator + uploadedFileName + "_" + DateUtility.getCurrentEpoch() + ext);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            bufferedOutputStream.write(bytes);
            return newFile;
        } catch (IOException ioException) {
            LoggerService.exception(ioException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LoggerService.exception(e);
                    throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                            ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
                }
            }
        }
    }

    /**
     * This method is used to write uploaded file on server. It can create thumb
     * nail of uploaded image base on given height and size.
     *
     * @param multipartFile - file
     * @param filePath      - Path where uploaded file will be stored
     * @param originalName  - name of file.
     * @param newName       - New name of file;
     * @return
     * @throws EndlosiotAPIException
     */
    public static File upload(MultipartFile multipartFile, String filePath, String originalName, String newName)
            throws EndlosiotAPIException {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            byte[] bytes = multipartFile.getBytes();

            File newFile = new File(filePath + File.separator + newName);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            bufferedOutputStream.write(bytes);
            return newFile;
        } catch (IOException ioException) {
            LoggerService.exception(ioException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LoggerService.exception(e);
                    throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                            ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
                }
            }
        }
    }

    /**
     * This method is used to write uploaded file on server. It can create thumb
     * nail of uploaded image base on given height and size.
     *
     * @param multipartFile
     * @param originalName
     * @return
     * @throws EndlosiotAPIException
     */
    public static File uploadCroppedImages(MultipartFile multipartFile, String filePath, String originalName)
            throws EndlosiotAPIException {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(filePath);

            if (!file.exists()) {
                file.mkdirs();
            }

            byte[] bytes = multipartFile.getBytes();

            String uploadedFileName = originalName.substring(0, originalName.lastIndexOf("."));
            String ext = originalName.substring(originalName.lastIndexOf("."));

            String newFileName = uploadedFileName + "_" + DateUtility.getCurrentEpoch() + ext;

            File newFile = new File(filePath + File.separator + newFileName);
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            bufferedOutputStream.write(bytes);
            return newFile;
        } catch (

                IOException ioException) {
            LoggerService.exception(ioException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LoggerService.exception(e);
                    throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                            ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
                }
            }
        }
    }

    /**
     * This method is used to create thumb nail of desired size from original image.
     *
     * @param image
     * @param width
     * @param height
     * @param resourceName
     * @return
     * @throws EndlosiotAPIException
     */
    public static String createThumbNail(File image, Integer width, Integer height, String resourceName)
            throws EndlosiotAPIException {
        try {
            String filePath = null;

            filePath = SystemSettingModel.getDefaultFilePath();

            File thumbNailFile = new File(
                    filePath + File.separator + resourceName + File.separator + "ThumbNail_" + image.getName());
            String extension = thumbNailFile.getName().substring(thumbNailFile.getName().lastIndexOf(".") + 1,
                    thumbNailFile.getName().length());

            BufferedImage originalBufferedImage = ImageIO.read(image);
            Dimension originalDimension = new Dimension(originalBufferedImage.getWidth(),
                    originalBufferedImage.getHeight());
            Dimension newDimension = new Dimension(width, height);
            Dimension ratioDimension = getScaledDimension(originalDimension, newDimension);
            BufferedImage newBufferedImage = new BufferedImage(Double.valueOf(ratioDimension.getWidth()).intValue(),
                    Double.valueOf(ratioDimension.getHeight()).intValue(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics()
                    .drawImage(originalBufferedImage.getScaledInstance(Double.valueOf(ratioDimension.getWidth()).intValue(),
                            Double.valueOf(ratioDimension.getHeight()).intValue(), Image.SCALE_SMOOTH), 0, 0, null);

            ImageIO.write(newBufferedImage, extension, thumbNailFile);
            return thumbNailFile.getName();
        } catch (IOException ioException) {
            LoggerService.exception(ioException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_CREATE_THUMBNAIL.getCode(),
                    ResponseCode.UNABLE_TO_CREATE_THUMBNAIL.getMessage());
        }
    }

    /**
     * This method is used to get new dimension ratio of it.
     *
     * @param originalDimension
     * @param newDimension
     * @return
     */
    private static Dimension getScaledDimension(Dimension originalDimension, Dimension newDimension) {
        int original_width = originalDimension.width;
        int original_height = originalDimension.height;
        int bound_width = newDimension.width;
        int bound_height = newDimension.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            // scale width to fit
            new_width = bound_width;
            // scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            // scale height to fit instead
            new_height = bound_height;
            // scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }
        return new Dimension(new_width, new_height);
    }

    /**
     * This method is used to download the files.
     *
     * @param path
     * @param isImage
     * @throws EndlosiotAPIException
     */
    public static void download(String path, boolean isImage) throws EndlosiotAPIException {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            File file = new File(path);
            inputStream = new FileInputStream(file);

            if (!isImage) {
                FileCopyUtils.copy(inputStream, WebUtil.getCurrentResponse().getOutputStream());
            } else {
                byte[] filebytes = IOUtils.toByteArray(inputStream);
                outputStream = WebUtil.getCurrentResponse().getOutputStream();
                outputStream.write(filebytes);
            }
        } catch (IOException ioException) {
            LoggerService.exception(ioException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_DOWNLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_DOWNLOAD_FILE.getMessage());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioException) {
                    LoggerService.exception(ioException);
                    throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_DOWNLOAD_FILE.getCode(),
                            ResponseCode.UNABLE_TO_DOWNLOAD_FILE.getMessage());
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ioException) {
                    LoggerService.exception(ioException);
                    throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_DOWNLOAD_FILE.getCode(),
                            ResponseCode.UNABLE_TO_DOWNLOAD_FILE.getMessage());
                }
            }
        }
    }

//	public static File createDefaultThumbNail(String shortName, String filePath) throws AlumnlyAPIException {
//		try {
//			File file = new File(filePath);
//			if (!file.exists()) {
//				file.mkdirs();
//			}
//			String newFileName = shortName + ".png";
//			File newFile = new File(file.getPath() + File.separator + newFileName);
//			if (newFile.exists()) {
//				return newFile;
//			}
//			File dummyFile = new File(WebUtil.getCurrentRequest().getServletContext().getRealPath(File.separator)
//					+ File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "userpic.png");
//			final BufferedImage image = ImageIO.read(dummyFile);
//			Graphics graphics = image.getGraphics();
//			graphics.setFont(new Font("", Font.BOLD, 120));
//			graphics.drawString(shortName, 150, 280);
//			graphics.dispose();
//			ImageIO.write(image, "png", newFile);
//			return newFile;
//		} catch (IOException e) {
//			LoggerService.exception(e);
//			return null;
//		}
//	}

    public static String createCaptchaImage(String filePath, String captcha, String uuid)
            throws EndlosiotAPIException {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            String newFileName = uuid + ".png";
            File newFile = new File(file.getPath() + File.separator + newFileName);
            if (newFile.exists()) {
                return newFileName;
            }
            File dummyFile = new File(WebUtil.getCurrentRequest().getServletContext().getRealPath(File.separator)
                    + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + "captcha.png");
            LoggerService.error("dummy File" + dummyFile);
            final BufferedImage image = ImageIO.read(dummyFile);
            Graphics2D graphics2d = image.createGraphics();

            graphics2d.setFont(new Font(Font.SANS_SERIF, 0, 20));
            graphics2d.setColor(Color.DARK_GRAY);
            graphics2d.drawString(captcha, 105, 26);
            graphics2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics2d.dispose();
            ImageIO.write(image, "png", newFile);
            return newFileName;
        } catch (IOException e) {
            LoggerService.exception(e);
            return null;
        }
    }

    /**
     * This method is used to store the bytes of the image.
     *
     * @param multipartFile
     * @param filePath
     * @return
     * @throws EndlosiotAPIException
     */
    public static File storeByteImage(MultipartFile multipartFile, String filePath) throws EndlosiotAPIException {
        BufferedOutputStream bufferedOutputStream = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            byte[] bytes = multipartFile.getBytes();

            String uploadedFileName = multipartFile.getName().substring(0, multipartFile.getName().lastIndexOf("."));
            String ext = multipartFile.getName().substring(multipartFile.getName().lastIndexOf("."));

            String newFileName = uploadedFileName + "_" + DateUtility.getCurrentEpoch() + ext;
            File newFile = new File(filePath + File.separator + newFileName);

            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(newFile));
            bufferedOutputStream.write(bytes);
            return newFile;
        } catch (IOException ioException) {
            LoggerService.exception(ioException);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
        } finally {
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    LoggerService.exception(e);
                    throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                            ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
                }
            }
        }
    }

    public static String convertToString(String filePath) throws EndlosiotAPIException {
        byte[] fileContent;
        try {
            fileContent = FileUtils.readFileToByteArray(new File(filePath));
        } catch (IOException e) {
            LoggerService.exception(e);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_CONVERT_INTO_BASE64.getCode(),
                    ResponseCode.UNABLE_TO_CONVERT_INTO_BASE64.getMessage());
        }
        return Base64.getEncoder().encodeToString(fileContent);
    }

    /**
     * This method is used to generate the compress images of jpeg file. This images
     * will be used in lazy loading.
     *
     * @param imageFile
     * @param filePath
     * @return
     * @throws EndlosiotAPIException
     */
    public static String compressImage(File imageFile, String filePath) throws EndlosiotAPIException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ImageWriter imageWriter = null;
        ImageOutputStream imageOutputStream = null;
        try {
            File compressedImageFile = new File(
                    filePath + File.separator + "Compress_ThumbNail_" + imageFile.getName());

            inputStream = new FileInputStream(imageFile);
            outputStream = new FileOutputStream(compressedImageFile);

            float quality = 0.001f;

            // create a BufferedImage as the result of decoding the supplied InputStream
            BufferedImage image = ImageIO.read(inputStream);

            // get all image writers for JPG format
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

            if (!writers.hasNext()) {
                throw new IllegalStateException("No writers found");
            }
            imageWriter = (ImageWriter) writers.next();
            imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam param = imageWriter.getDefaultWriteParam();

            // compress to a given quality
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // appends a complete image stream containing a single image and
            // associated stream and image metadata and thumbnails to the output
            imageWriter.write(null, new IIOImage(image, null, null), param);
            return compressedImageFile.getName();
            // close all streams

        } catch (IOException e) {
            LoggerService.exception(e);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (imageOutputStream != null) {
                    imageOutputStream.close();
                }
                if (imageWriter != null) {
                    imageWriter.dispose();
                }
            } catch (IOException e) {
                LoggerService.exception(e);
                throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                        ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
            }

        }

    }

    /**
     * This method is used to compress the png images. In case of png images first
     * we need to create jpeg from png and convert those jpeg image into compressed
     * file.
     *
     * @param imageFile
     * @param filePath
     * @return
     * @throws EndlosiotAPIException
     */
    public static String compressPNGImage(File imageFile, String filePath) throws EndlosiotAPIException {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        ImageWriter imageWriter = null;
        ImageOutputStream imageOutputStream = null;
        try {
            File compressedImageFile = new File(filePath + File.separator + "Compress_ThumbNail_"
                    + imageFile.getName().substring(0, imageFile.getName().lastIndexOf(".") + 1) + "jpg");
            File intermediateFile = new File(filePath + File.separator + "JPEG_"
                    + imageFile.getName().substring(0, imageFile.getName().lastIndexOf(".") + 1) + "jpg");

            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(imageFile);
                BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(), bufferedImage.getHeight(),
                        BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);
                ImageIO.write(newBufferedImage, "jpg", intermediateFile);
                bufferedImage.flush();
            } catch (IOException ioException) {
                LoggerService.exception(ioException);
                throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                        ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
            }

            // inputStream = new FileInputStream(imageFile);
            inputStream = new FileInputStream(intermediateFile);
            outputStream = new FileOutputStream(compressedImageFile);

            float quality = 0.001f;

            // create a BufferedImage as the result of decoding the supplied InputStream
            BufferedImage image = ImageIO.read(inputStream);

            // get all image writers for JPG format
            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");

            if (!writers.hasNext()) {
                throw new IllegalStateException("No writers found");
            }
            imageWriter = (ImageWriter) writers.next();
            imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam param = imageWriter.getDefaultWriteParam();

            // compress to a given quality
            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(quality);

            // appends a complete image stream containing a single image and
            // associated stream and image metadata and thumbnails to the output
            imageWriter.write(null, new IIOImage(image, null, null), param);
            return compressedImageFile.getName();
            // close all streams

        } catch (IOException e) {
            LoggerService.exception(e);
            throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                    ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
        } finally {
            try {
                inputStream.close();
                outputStream.close();
                imageOutputStream.close();
                imageWriter.dispose();
            } catch (IOException e) {
                LoggerService.exception(e);
                throw new EndlosiotAPIException(ResponseCode.UNABLE_TO_UPLOAD_FILE.getCode(),
                        ResponseCode.UNABLE_TO_UPLOAD_FILE.getMessage());
            }

        }
    }

    /**
     * This method is used to copy files from source to destination in same system.
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFileUsingFileChannels(File source, File dest) throws IOException {

        FileChannel inputChannel = null;
        FileChannel outputChannel = null;
        try {
            inputChannel = new FileInputStream(source).getChannel();
            outputChannel = new FileOutputStream(dest).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
    }

    /**
     * This method is used to get file Extension from name.
     *
     * @param originalName
     */
    public static String extractFileExtension(String originalName) {
        return originalName.substring(originalName.lastIndexOf(".") + 1);
    }

    public static void downloadExcel(String fileName, Workbook workbook) {
        setCookie(fileName, DateUtility.getCurrentEpoch());
        try {
            workbook.write(WebUtil.getCurrentResponse().getOutputStream());
        } catch (IOException e) {
            LoggerService.exception(e);
        }
    }

    public static void setCookie(String fileName, Long uploadTime) {
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());

        switch (ImageFileExtensionEnum.fromId(fileExtension)) {
            case XLS:
                WebUtil.getCurrentResponse().setContentType("application/xls");
                break;
            case XLSX:
                WebUtil.getCurrentResponse().setContentType("application/xlsx");
                break;
            default:
                break;
        }

        WebUtil.getCurrentResponse().setHeader("Content-disposition", "attachment; filename=\"" + fileName + "\"");
        WebUtil.getCurrentResponse().setHeader("Cache-control", "max-age=31536000");
        WebUtil.getCurrentResponse().setHeader("Last-Modified", uploadTime.toString());
    }

//	/**
//	 * This method is used to get column count of excel.
//	 *
//	 * @param file
//	 * @throws IOException
//	 */
//	public static Long getExcelTotalColumn(File file) throws IOException {
//		FileInputStream fis = null;
//		XSSFWorkbook wb = null;
//		XSSFRow row = null;
//		try {
//			fis = new FileInputStream(file);
//			wb = new XSSFWorkbook(fis);
//			XSSFSheet sheet = wb.getSheetAt(0);
//			row = sheet.getRow(0);
//		} finally {
//			if (wb != null) {
//				wb.close();
//			}
//			if (fis != null) {
//				fis.close();
//			}
//		}
//		return Long.valueOf(row == null ? 0 : row.getLastCellNum());
//	}
}